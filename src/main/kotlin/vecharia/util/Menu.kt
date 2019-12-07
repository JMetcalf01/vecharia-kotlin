package vecharia.util

import com.badlogic.gdx.Input
import vecharia.Vecharia
import java.util.*
import kotlin.collections.LinkedHashMap

class Menu(private val title: String, private val centered: Boolean = false) {
    private val order: LinkedList<String> = LinkedList()
    private val selections: LinkedHashMap<String, () -> Unit> = LinkedHashMap()

    fun selection(title: String, onSelect: () -> Unit) {
        order.add(title)
        selections[title] = onSelect
    }

    fun render(game: Vecharia) {
        var selection: Int = 0


        game.addInputEvent(Input.Keys.UP) {
            selection
        }
        game.addInputEvent(Input.Keys.DOWN) {

        }
        game.addInputEvent(Input.Keys.ENTER) {
            selections[order[selection]]?.invoke()
            selection = -1
        }
        while (selection != -1)
            game.sleep(10)
        game.removeInputEvent(Input.Keys.UP)
        game.removeInputEvent(Input.Keys.DOWN)
        game.removeInputEvent(Input.Keys.ENTER)
    }

    private fun render(game: Vecharia, selection: Int) {
        game.print(title)
        for (i in order.indices) {
            val title = order[i]
//            title
        }
    }
}