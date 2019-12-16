package vecharia.menu

import vecharia.Vecharia
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
 * @param game the Vecharia game instance
 */
class PauseMenu(game: Vecharia) : Menu(game, "Pause Menu", centered = true) {
    init {
        selection("Resume") {
            GameState.state = GameState.ACTIVE
        }

        selection("Save") {
            game.log.error("Saving not implemented!")
            // todo implement saving
            game.render(PauseMenu(game))
        }

        selection("Settings") {
            game.render(SettingsMenu(game, this))
        }

        selection("Exit") {
            // Save
            game.log.warn("Saving not implemented")
            // todo implement saving

            // Exit
            game.exit()
        }
    }
}