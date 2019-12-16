package vecharia.menu

import vecharia.Vecharia
import vecharia.util.GameState

/**
 * Creates the start menu with selections:
 *     New Game (creates new game)
 *     Load Game (loads game from save file)
 *     Settings (opens settings menu)
 *     Credits (runs the credits)
 *     Exit (exits the game)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @param game the Vecharia game instance
 */
class StartMenu(game: Vecharia) : Menu(game,"Welcome to Vecharia!", centered = true) {
    init {
        selection("New Game") {
            GameState.state = GameState.ACTIVE
            game.log.info("new game -- state = ${GameState.state}")
            game.printer.clear()
        }

        selection("Load Game") {
            game.log.warn("Load game in progress!")
            game.render(StartMenu(game))
            // todo implement loading game
        }

        selection("Settings") {
            game.log.info("Settings opened")
            game.render(SettingsMenu(game, this))
        }

        selection("Credits") {
            game.log.info("Running credits")
            game.render(StartMenu(game))
            // todo implement credits
        }

        selection("Exit") {
            game.exit()
        }
    }
}