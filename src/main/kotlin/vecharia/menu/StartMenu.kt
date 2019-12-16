package vecharia.menu

import com.badlogic.gdx.Gdx
import vecharia.Vecharia
import vecharia.render.SoundSystem
import vecharia.util.GameState
import kotlin.system.exitProcess

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
 */
class StartMenu(game: Vecharia) : Menu(game,"Welcome to Vecharia!", centered = true) {
    init {
        selection("New Game") {
            GameState.state = GameState.ACTIVE
            game.log.info("New game started and GameState is now Active")
        }

        selection("Load Game") {
            game.log.warn("Load game in progress!")
            // todo implement loading game
        }

        selection("Settings") {
            game.log.info("Settings opened")
            game.render(SettingsMenu(game))
        }

        selection("Credits") {
            game.log.info("Running credits")
            // todo implement credits
        }

        selection("Exit") {
            game.log.info("Exiting")
            game.window.dispose()
            Gdx.app.exit()
            SoundSystem.end()
            exitProcess(0)
        }
    }
}