package vecharia

import vecharia.entity.Player
import vecharia.logging.Logger
import vecharia.menu.Menu
import vecharia.menu.StartMenu
import vecharia.render.Printer
import vecharia.render.Window
import vecharia.util.GameState
import vecharia.util.Tickable


/**
 * The main game class.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
class Vecharia(val log: Logger, val window: Window) : Tickable {
    val printer: Printer = Printer(window.canvas, GameState.ACTIVE)
    private val tickables: MutableSet<Tickable> = mutableSetOf(printer)

    lateinit var player: Player

    /**
     * Starts the game thread.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun start() {
        val startMenu = StartMenu(this)
        log.info("Entering Start Menu")
        render(startMenu)
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
    fun render(menu: Menu, parent: Menu? = null) {
        synchronized(tickables) {
            tickables.add(menu)
        }
        menu.render(parent) {
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