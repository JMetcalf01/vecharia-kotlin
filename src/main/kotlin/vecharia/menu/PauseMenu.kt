package vecharia.menu

import vecharia.Vecharia
import java.util.concurrent.atomic.AtomicBoolean

object PauseMenu {
    private val menu: Menu = Menu("Vecharia", centered = true)
    val open: AtomicBoolean = AtomicBoolean(false)

    init {
        menu.selection("placeholder 1") {
            println("placeholder 1")
        }
        menu.selection("placeholder 2") {
            println("placeholder 2")
        }
        menu.selection("placeholder 3") {
            println("placeholder 3")
        }
        menu.selection("back") {
            open.set(false)
        }
    }

    fun open(game: Vecharia) {
        println("Opening")
        open.set(true)
        game.paused.set(true)
//        paused.set(true)

//        Thread() {
//            game.getMenuInput(menu)
//            while (open.get())
//                game.sleep(5)
//            game.paused.set(false)
//        }.start()
    }

    fun close(game: Vecharia) {
        println("Closing")
        open.set(false)
        game.paused.set(false)
    }
}