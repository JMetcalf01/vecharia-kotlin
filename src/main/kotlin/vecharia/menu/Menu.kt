package vecharia.menu

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.graphics.Color
import vecharia.Input
import vecharia.Vecharia
import vecharia.render.Text
import vecharia.util.GameState
import vecharia.util.Tickable
import java.util.*

/**
 * A menu to be displayed in the game.
 *
 * @author Matt Worzala
 * @since 1.3
 *
 * @param title the title of the menu
 * @param centered whether the menu is centered on the screen
 */
open class Menu(val game: Vecharia, private val title: String, var closeOnSelect: Boolean = true, private val caret: Boolean = true, private val centered: Boolean = false) : Tickable {
    private val selections: MutableList<Selection> = LinkedList()
    private var current: Int = 0
    private var refresh: Boolean = true

    /**
     * Add a new selection option to the menu
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param title the title of the option
     * @param onSelect the function to be called upon selection
     */
    fun selection(title: String, onSelect: (Selection) -> Unit) {
        selections.add(Selection(this, title, onSelect))
    }

    /**
     * Renders the menu --   this method should not be called manually.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param callback a callback for when the menu has been closed by a a selction
     */
    fun render(callback: () -> Unit = {}) {
        val up = Input.registerListener(UP, GameState.state) {
            if (current > 0) current--
            refresh = true
        }
        val down = Input.registerListener(DOWN, GameState.state) {
            if (current < selections.size - 1) current++
            refresh = true
        }
        var enter: Int = -1
        enter = Input.registerListener(ENTER, GameState.state) {
            val selection = selections[current]
            selection.callback(selection)
            current = 0
            if (closeOnSelect) {
                Input -= up
                Input -= down
                Input -= enter
                callback()
            } else refresh = true
        }
    }

    /**
     * Ticks the menu.
     * 
     * @author Jonathan Metcalf
     * @since 1.3
     * 
     * @param game the Vecharia game instance
     * @param frame the current frame
     */
    override fun tick(game: Vecharia, frame: Long) {
        if (!refresh) return
        refresh = false

        game.printer.clear()

        // Centers vertically
        if (centered) { //todo fix the length of the titles here (pad them so that they all match the length of the longest one val titleFixed: String = title.padEnd(fixLengths(order))
            for (i in 0 until (game.window.charHeight() / 2) - (selections.size + 1)) {
                game.printer += Text(" ", newLine = true, instant = true)
            }
        }

        // Centers title horizontally
        if (centered) {
            for (j in 0..centerString(game, title)) {
                game.printer += Text(" ", newLine = false, instant = true)
            }
        }
        game.printer += Text(title, instant = true)

        val titles = selections.map { it.title }
        val longest = titles.map { it.length }.sortedDescending()[0]
        var i = 0
        titles.map { it.padEnd(longest, ' ') }.forEach {
            if (centered)
                game.printer += Text("".padEnd(centerString(game, "  $it")), newLine = false, instant = true)

            if (i == current)
                game.printer += Text("${if (caret) "> " else ""}$it", Color.FOREST, instant = true)
            else game.printer += Text("${if (caret) "  " else ""}$it", instant = true)

            i++
        }
    }

    /**
     * Returns the number of spaces to center a certain string.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param game the Vecharia game instance
     * @param string the string to be centered
     * @return the number of spaces to center the string
     */
    private fun centerString(game: Vecharia, string: String): Int {
        return game.window.charWidth() / 2 - string.length / 2
    }

    /**
     * This method standardizes the length of all the options
     * It determines the length of the longest String in the array
     * passed in, then adds spaces to every line that has a length
     * less than that.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param list the options to be standardized in length
     * @return the length of the longest string
     */
    private fun fixLengths(list: MutableList<String>): Int {
        var maxLength = 0
        for (i in list.indices) {
            if (maxLength < list[i].length)
                maxLength = list[i].length
        }

        for (i in list.indices)
            while (list[i].length < maxLength)
                list[i] += " "

        return maxLength
    }
}

/**
 * Represents a selection option in a menu and enables changing the title of a selection to reflect a change.
 *
 * @author Matt Worzala
 * @since 1.3
 *
 * @param menu the menu instance that the selection belongs to
 * @param title the title of the selection
 * @param callback the function to be called when the selection is chosen
 */
data class Selection(val menu: Menu, var title: String, val callback: (Selection) -> Unit)