package vecharia.menu

import com.badlogic.gdx.Input
import vecharia.Vecharia
import vecharia.util.GameState
import java.util.concurrent.atomic.AtomicBoolean

object MainMenu {
    private lateinit var game: Vecharia

    private val menu: Menu = Menu("Vecharia", centered = true).selection("placeholder 1") {
        println("placeholder 1")
    }.selection("placeholder 2") {
        println("placeholder 2")
    }.selection("placeholder 3") {
        println("placeholder 3")
    }.selection("back") {
        GameState.paused = false
    }

    fun register(vecharia: Vecharia) {
        game = vecharia
        GameState.onPause { menu.render(game) }
        game.addInputEvent(Input.Keys.ESCAPE) { GameState.paused = true }
        game.addInputEvent(Input.Keys.ESCAPE, true) { GameState.paused = false }
    }

    fun tick(frame: Int) {
        menu.tick(game)
    }
}