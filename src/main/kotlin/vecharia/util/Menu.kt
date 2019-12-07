package vecharia.util

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import vecharia.Vecharia
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashMap

class Menu(private val title: String, private val centered: Boolean = false) {
    private val order: LinkedList<String> = LinkedList()
    private val selections: LinkedHashMap<String, () -> Unit> = LinkedHashMap()

    fun selection(title: String, onSelect: () -> Unit) {
        order.add(title)
        selections[title] = onSelect
    }

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