package vecharia

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import vecharia.logging.Logger
import vecharia.render.GameThread
import vecharia.render.Window
import vecharia.menu.Menu
import vecharia.menu.PauseMenu
import vecharia.render.Printer
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
    lateinit var gameThread: GameThread
    lateinit var printer: Printer

    private val tickables: MutableSet<Tickable> = HashSet()
    val pauseMenu: PauseMenu = PauseMenu(this)

    /**
     * Starts the game thread.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun start() {
        printer = Printer(this, window.canvas)
        gameThread = GameThread(this)
    }

    /**
     * Returns whether the user is currently typing.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return whether the user is typing
     */
    fun isTyping(): Boolean = window.entering

    /**
     * Adds a keybind for a specific action.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key to trigger the action
     * @param onInput the action to be performed
     */
    fun addInputEvent(key: Int, onInput: () -> Unit) = window.addKeyAction(key, onInput)

    /**
     * Removes a specific keybind.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key for the keybind to remove
     */
    fun removeInputEvent(key: Int) = window.removeKeyAction(key)

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
}