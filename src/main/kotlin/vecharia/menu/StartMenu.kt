package vecharia.menu

import vecharia.Vecharia
import vecharia.introduction.Introduction
import vecharia.render.Text
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
 * @constructor initializes the selections
 *
 * @param game the Vecharia game instance
 */
class StartMenu(game: Vecharia) : Menu(game,"Welcome to Vecharia!", closeOnSelect = false, centered = true) {
    init {
        selection("New Game") {
            it.menu.closeOnSelect = true
            game.render(SaveSelectionMenu(game))
        }

        selection("Load Game") {
            game.log.warn("Load game in progress!")

            // todo implement loading game
        }

        selection("Settings") {
            it.menu.closeOnSelect = true
            game.render(SettingsMenu(game, this))
        }

        selection("Credits") {
            it.menu.closeOnSelect = false
            game.printer.clear()
            game.printer += "Sorry, credits have not been implemented!"
            game.printer += Text("The game can be quit using Alt+F4 (Windows, Linux) or Command+Q (macOS).", wait = true)
        }

        selection("Exit") {
            game.exit()
        }
    }
}

/**
 * A menu for the save selection options
 *
 * @author Matt Worzala
 * @since 1.3
 *
 * @constructor initializes the selections
 *
 * @param game the Vecharia game instance
 */
class SaveSelectionMenu(game: Vecharia) : Menu(game, "Select a save:", centered = true) {
    init {
        selection("Slot 0: Empty") {
            it.menu.closeOnSelect = true
            it.title = "Slot 0: NEW_CHARACTER"
            GameState.state = GameState.ACTIVE
            Introduction(game).introduction().then { player ->
                game.player = player
            }
        }
        selection("Slot 1: Empty", this::newGame)
        selection("Slot 2: Empty", this::newGame)
        selection("Back") { game.render(StartMenu(game)) }
    }

    /**
     * Starts a new game (eventually)
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param selection is currently unused
     */
    private fun newGame(selection: Selection) {
        GameState.state = GameState.ACTIVE
        game.log.info("new game -- state = ${GameState.state}")
        game.printer.clear()
    }
}