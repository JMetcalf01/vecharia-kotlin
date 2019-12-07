package vecharia.menu

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import vecharia.Vecharia
import vecharia.util.GameState
import vecharia.util.Tickable
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashMap
import kotlinx.coroutines.GlobalScope

/**
 * A menu to be displayed in the game.
 *
 * @author Matt Worzala
 * @since 1.0
 *
 * @param title the title of the menu
 * @param centered whether the menu is centered on the screen
 */
class Menu(private val title: String, private val centered: Boolean = false) : Tickable {
    private val order: LinkedList<String> = LinkedList()
    private val selections: LinkedHashMap<String, () -> Unit> = LinkedHashMap()
    private var lastSelection: Int = 0

    private val _selection: AtomicInteger = AtomicInteger(-1)
    val selection: Int get() = _selection.get()

    var ready: Boolean = false
        private set

    /**
     * Add a new selection option to the menu
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param title the title of the option
     * @param onSelect the function to be called upon selection
     */
    fun selection(title: String, onSelect: () -> Unit): Menu {
        order.add(title)
        selections[title] = onSelect
        return this
    }

    override fun tick(game: Vecharia) {
        println("1")
        if (lastSelection != selection) {
            println("2")
            val delay = if (selection == -1) 20L else 0L
            game.clear()
            println("3")
            game.print(title, delay = delay)
            println("4")
            for (i in order.indices) {
                val option = order[i]
                if (i == selection)
                    game.print("> $option", Color.GREEN, delay = delay)
                else game.print("  $option", delay = delay)
            }
        }
        lastSelection = selection
    }

    suspend fun print() {

    }

    fun render(game: Vecharia) {
        game.addInputEvent(Input.Keys.UP, GameState.paused) {
            if (_selection.get() > 0)
                _selection.decrementAndGet()
        }
        game.addInputEvent(Input.Keys.DOWN, GameState.paused) {
            println("DOWN")
            if (_selection.get() < order.size - 1)
                _selection.incrementAndGet()
            println("DOWN ->>>> $selection")
        }
        game.addInputEvent(Input.Keys.ENTER, GameState.paused) {
            selections[order[_selection.get()]]?.invoke()
            complete(game)
        }
        ready = false
        lastSelection = 0
        _selection.set(-1)
        tick(game)
        _selection.set(0)
        tick(game)
    }

    private fun complete(game: Vecharia) {
        game.removeInputEvent(Input.Keys.UP, GameState.paused)
        game.removeInputEvent(Input.Keys.DOWN, GameState.paused)
        game.removeInputEvent(Input.Keys.ENTER, GameState.paused)
        ready = true
    }
}