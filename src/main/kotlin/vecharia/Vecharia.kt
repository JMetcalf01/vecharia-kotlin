package vecharia

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import vecharia.logging.Logger
import vecharia.menu.Menu
import vecharia.menu.StartMenu
import vecharia.render.Printer
import vecharia.render.Window
import vecharia.util.GameState
import vecharia.util.Tickable
import java.awt.Toolkit

private const val FULLSCREEN: Boolean = true

/**
 * The entry point into the program.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
fun main() {
    val config = LwjglApplicationConfiguration()
    config.height = Toolkit.getDefaultToolkit().screenSize.height
    config.width = Toolkit.getDefaultToolkit().screenSize.width
    config.fullscreen = FULLSCREEN
    config.resizable = false

    LwjglApplication(Window(), config)
}


/**
 * The main game class.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
class Vecharia(val log: Logger, val window: Window) : Tickable {
    val printer: Printer = Printer(window.canvas, GameState.ACTIVE)
    private val tickables: MutableSet<Tickable> = mutableSetOf(printer)

    /**
     * Starts the game thread.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun start() {
//        SoundSystem.add("assets/introscreen.mp3", looping = true, volume = 1f)
//        SoundSystem.playM()
//        log.info("Sound system playing")

        val startMenu = StartMenu(this)
        log.info("Entering Start Menu")
        render(startMenu)

//        Input.readInput().then {
//            println(it)
//
//        }
    }

    /**
     * Given a menu to render, adds it to tickables, renders it,
     * and when the user leaves the menu, removes it from tickables.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @param menu the menu to render
     */
    fun render(menu: Menu) {
        synchronized(tickables) {
            tickables.add(menu)
        }
        menu.render(this) {
            synchronized(tickables) {
                tickables.remove(menu)
            }
        }
    }

    /**
     * Ticks every single tickable.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @param game the Vecharia game instance
     * @param frame the current frame count
     */
    override fun tick(game: Vecharia, frame: Long) {
        synchronized(tickables) {
            for (tickable in tickables) {
                tickable.tick(game, frame)
            }
        }
    }

    /**
     * Exits the game safely
     *
     * @author Matt Worzala
     * @since 1.2
     */
    fun exit() {
        window.exit = true
    }

}