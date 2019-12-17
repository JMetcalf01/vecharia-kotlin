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
class StartMenu(game: Vecharia) : Menu(game,"Welcome to Vecharia!", closeOnSelect = false, centered = true) {
    init {
        selection("New Game") {
            game.log.info("Prompting for selection")
            it.menu.closeOnSelect = true
            game.render(SaveSelectionMenu(game))
        }

        selection("Load Game") {
            game.log.warn("Load game in progress!")

            // todo implement loading game
        }

        selection("Settings") {
            game.log.info("Settings opened")
            it.menu.closeOnSelect = true
            game.render(SettingsMenu(game, this))
        }

        selection("Credits") {
            game.log.info("Running credits")
            // todo implement credits
        }

        selection("Exit") {
            game.exit()
        }
    }
}

class SaveSelectionMenu(game: Vecharia) : Menu(game, "Select a save:", centered = true) {
    init {
        selection("Slot 0: Empty") {
            it.menu.closeOnSelect = false
            it.title = "Slot 0: NEW_CHARACTER"
        }
        selection("Slot 1: Empty", this::newGame)
        selection("Slot 2: Empty", this::newGame)
        selection("Back") { game.render(StartMenu(game)) }
    }

    private fun newGame(selection: Selection) {
        GameState.state = GameState.ACTIVE
        game.log.info("new game -- state = ${GameState.state}")
        game.printer.clear()
    }
}