package vecharia.menu

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.graphics.Color
import vecharia.Input
import vecharia.Vecharia
import vecharia.render.Printer
import vecharia.render.Text
import vecharia.util.GameState
import vecharia.util.Promise
import vecharia.util.State
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
open class Menu(
    val game: Vecharia,
    private val title: String,
    var closeOnSelect: Boolean = true,
    private val caret: Boolean = true,
    private val centered: Boolean = false,
    private val printer: Printer = game.printer,
    private val state: State? = null
) : Tickable {

    /**
     * Creates a basic promise based menu. The menu simply consists of a set of immutable options and returns a promise representing the index of the selected option.
     *
     * @author Matt Worzala
     * @since 1.3
     */
    companion object {
        /**
         * Creates a basic promise based menu. The menu simply consists of a set of immutable options and returns a promise representing the index of the selected option.
         *
         * @author Matt Worzala
         * @since 1.3
         *
         * @param game the vecharia game instance
         * @param title the title of the menu
         * @param options the options to be rendered, the order determines the indices
         * @param centered whether the menu is centered on the screen
         * @returns a promise of the selected index
         */
        fun basic(game: Vecharia, title: String, vararg options: String, centered: Boolean = false): Promise<Int> =
            Promise {
                if (options.isEmpty())
                    throw IllegalArgumentException("Must provide at least one option.")

                val menu = Menu(game, title, centered = centered)
                for (i in options.indices)
                    menu.selection(options[i]) { it(i) }
                game.render(menu)
            }
    }

    private val selections: MutableList<Selection> = LinkedList()
    protected var current: Int = 0
    protected var refresh: Boolean = true

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
     * Renders the menu -- this method should not be called manually.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param callback a callback for when the menu has been closed by a selection
     */
    fun render(callback: () -> Unit = {}) {
        val up = Input.registerListener(UP, state ?: GameState.state) {
            if (current > 0) current--
            refresh = true
        }
        val down = Input.registerListener(DOWN, state ?: GameState.state) {
            if (current < selections.size - 1) current++
            refresh = true
        }
        var enter: Int = -1
        enter = Input.registerListener(ENTER, state ?: GameState.state) {
            val selection = selections[current]
            game.log.debug("Option '${selection.title.trim()}' has been selected.")
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

        printer.clear()

        // Centers vertically
        if (centered) {
            for (i in 0 until (game.window.charHeight() / 2) - (selections.size + 1)) {
                printer += Text(" ", newLine = true, instant = true)
            }
        }

        // Centers title horizontally
        if (centered) {
            for (j in 0..centerString(game, title)) {
                printer += Text(" ", newLine = false, instant = true)
            }
        }
        printer += Text(title, instant = true)

        val titles = selections.map { it.title }
        val longest = titles.map { it.length }.sortedDescending()[0]
        var i = 0
        titles.map { it.padEnd(longest, ' ') }.forEach {
            if (centered)
                printer += Text("".padEnd(centerString(game, "  $it")), newLine = false, instant = true)

            if (i == current)
                printer += Text("${if (caret) "> " else ""}$it", Color.FOREST, instant = true)
            else printer += Text("${if (caret) "  " else ""}$it", instant = true)

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
