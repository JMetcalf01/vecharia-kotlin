package vecharia.util

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
        order.add(title)
        selections[title] = onSelect
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

        render(game, selection.get(), 20)
        while (selection.get() != -1)
            game.sleep(5)
        game.removeInputEvent(Input.Keys.UP)
        game.removeInputEvent(Input.Keys.DOWN)
        game.removeInputEvent(Input.Keys.ENTER)
    }

    /**
     * Render the text forming the menu.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param game the vecharia game instance
     * @param selection the currently selected item, where 0 is the top item
     * @param delay the delay between printing each item.
     */
    private fun render(game: Vecharia, selection: Int, delay: Long = 0) {
        game.clear()
        game.print(title, delay = delay)
        for (i in order.indices) {
            val option = order[i]
            if (i == selection)
                game.print("> $option", Color.GREEN, delay = delay)
            else game.print("  $option", delay = delay)
        }
    }
}