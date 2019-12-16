package vecharia.menu

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.graphics.Color
import vecharia.Input
import vecharia.Vecharia
import vecharia.render.Text
import vecharia.util.GameState
import vecharia.util.Tickable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashMap

/**
 * A menu to be displayed in the game.
 *
 * @author Matt Worzala
 * @since 1.0
 *
 * @param title the title of the menu
 * @param centered whether the menu is centered on the screen
 */
open class Menu(val game: Vecharia, private val title: String, private val centered: Boolean = false) : Tickable {
    private val order: LinkedList<String> = LinkedList()
    private val selections: LinkedHashMap<String, () -> Unit> = LinkedHashMap()
    private val selection: AtomicInteger = AtomicInteger(0)
    private val lastSelection: AtomicInteger = AtomicInteger(-1)

    /**
     * Add a new selection option to the menu
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param title the title of the option
     * @param onSelect the function to be called upon selection
     */
    fun selection(title: String, onSelect: () -> Unit) {
        val titleFixed: String = title.padEnd(fixLengths(order))
        order.add(titleFixed)
        selections[titleFixed] = onSelect
    }

    /**
     * Renders the menu --   this method should not be called manually.
     *
     * @author Matt Worzala
     * @since 1.1
     *
     * @param game the vecharia game instance
     */
    fun render(game: Vecharia, callback: () -> Unit = {}) {
        val up = Input.registerListener(UP, GameState.state) {
            if (selection.get() > 0)
                selection.decrementAndGet()
        }
        val down = Input.registerListener(DOWN, GameState.state) {
            if (selection.get() < order.size - 1)
                selection.incrementAndGet()
        }
        Input.registerListener(ENTER, GameState.state, once = true) {
            selections[order[selection.get()]]?.invoke()
            selection.set(0)
            Input -= up
            Input -= down
            callback()
        }
    }

    /**
     * Ticks the menu.
     * 
     * @author Jonathan Metcalf
     * @since 1.1
     * 
     * @param game the Vecharia game instance
     * @param frame the current frame
     */
    override fun tick(game: Vecharia, frame: Long) {
        if (lastSelection.get() == selection.get())
            return

        game.printer.clear()

        // Centers vertically
        if (centered) {
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

        for (i in order.indices) {
            val option = order[i]

            // Centers options horizontally
            if (centered)
                game.printer += Text("".padEnd(centerString(game, "  $option")), newLine = false, instant = true)

            if (i == selection.get())
                game.printer += Text("> $option", Color.FOREST, instant = true)
            else
                game.printer += Text("  $option", instant = true)
        }

        lastSelection.set(selection.get())
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