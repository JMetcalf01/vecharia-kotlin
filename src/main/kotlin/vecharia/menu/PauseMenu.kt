package vecharia.menu

import vecharia.Input
import vecharia.Vecharia
import vecharia.render.Printer
import vecharia.util.GameState

/**
 * Creates the pause menu with selections:
 *     Resume (unpauses game)
 *     Save (saves game to file)
 *     Settings (opens settings menu)
 *     Exit (saves and exits the game)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @constructor makes the list of selections, registers the escape keybind, and renders the menu
 *
 * @param game the Vecharia game instance
 * @param printer the printer instance
 */
class PauseMenu(game: Vecharia, private val printer: Printer = Printer(game.window.canvas, GameState.PAUSED)) : Menu(game, "Pause Menu", closeOnSelect = false, centered = true, printer = printer, state = GameState.PAUSED) {

    init {
        selection("Resume") { unpause() }
        selection("Save") {
            game.log.error("Saving not implemented!")
        }
        selection("Inventory") {
            game.log.error("Inventory not implemented!")
        }
        selection("Settings") {
            println("SETT")
//            game.render(SettingsMenu(game, this)) todo this wont work because game needs to be ticking for that. we will need a different solution there
        }
        selection("Exit") {
            // Save
            game.log.warn("Saving not implemented")

            // Exit
            game.exit()
        }

        Input.registerListener(com.badlogic.gdx.Input.Keys.ESCAPE, GameState.ACTIVE, callback = this::pause)
        Input.registerListener(com.badlogic.gdx.Input.Keys.ESCAPE, GameState.PAUSED, callback = this::unpause)

        this.render()
    }

    /**
     * Ticks the pause menu when called.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param game the Vecharia game instance
     * @param frame the current frame of the game
     */
    override fun tick(game: Vecharia, frame: Long) {
        printer.tick(game, frame)

        super.tick(game, frame)
    }

    /**
     * Pauses the game overall.
     *
     * @author Matt Worzala and Jonathan Metcalf
     * @since 1.3
     */
    private fun pause() {
        game.window.canvas.buffer()
        GameState.state = GameState.PAUSED
    }

    /**
     * Unpauses the game overall.
     *
     * @author Matt Worzala and Jonathan Metcalf
     * @since 1.3
     */
    private fun unpause() {
        super.current = 0
        super.refresh = true
        GameState.state = GameState.ACTIVE
        game.window.canvas.unbuffer()
    }
}