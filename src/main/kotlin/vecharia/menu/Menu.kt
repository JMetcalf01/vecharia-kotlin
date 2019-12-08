package vecharia.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import vecharia.Vecharia
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
class Menu(private val title: String, private val centered: Boolean = false) {
    private val order: LinkedList<String> = LinkedList()
    private val selections: LinkedHashMap<String, () -> Unit> = LinkedHashMap()

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
     * Render the menu, this method should not be called manually.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param game the vecharia game instance
     */
    fun render(game: Vecharia) {
        val selection = AtomicInteger(0)
        game.addInputEvent(Input.Keys.UP) {
            if (selection.get() > 0)
                selection.decrementAndGet()
            render(game, selection.get())
        }
        game.addInputEvent(Input.Keys.DOWN) {
            if (selection.get() < order.size - 1)
                selection.incrementAndGet()
            render(game, selection.get())
        }
        game.addInputEvent(Input.Keys.ENTER) {
            selections[order[selection.get()]]?.invoke()
            selection.set(-1)
        }

        render(game, selection.get())
        while (selection.get() != -1)
            game.sleep(5)
        game.removeInputEvent(Input.Keys.UP)
        game.removeInputEvent(Input.Keys.DOWN)
        game.removeInputEvent(Input.Keys.ENTER)
    }

    /**
     * Render the text forming the menu.
     *
     * @author Matt Worzala and Jonathan Metcalf
     * @since 1.0
     *
     * @param game the Vecharia game instance
     * @param selection the currently selected item, where 0 is the top item
     */
    private fun render(game: Vecharia, selection: Int) {
        game.clear()

        // Centers vertically
        if (centered) {
            for (i in 0 until (game.window.charHeight() / 2) - (selections.size + 1)) {
                game.print("", newLine = true)
            }
        }

        // Centers title horizontally
        if (centered) {
            for (j in 0..centerString(game, title)) {
                game.print(" ", newLine = false, delay = 0)
            }
        }
        game.print(title, delay = 0)

        for (i in order.indices) {
            val option = order[i]

            // Centers options horizontally
            for (j in 0..centerString(game, "  $option")) {
                game.print(" ", newLine = false, delay = 0)
            }

            if (i == selection) game.print("> $option", Color.GREEN, delay = 0)
            else game.print("  $option", delay = 0)
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