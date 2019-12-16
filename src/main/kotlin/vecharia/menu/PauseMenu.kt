package vecharia.menu

import com.badlogic.gdx.Gdx
import vecharia.Vecharia
import vecharia.render.SoundSystem
import vecharia.util.GameState
import kotlin.system.exitProcess

/**
 * Creates the pause menu with selections:
 *     Resume (unpauses game)
 *     Save (saves game to file)
 *     Settings (opens settings menu)
 *     Save and Exit Game (exits the game)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 */
class PauseMenu(game: Vecharia) : Menu(game, "Pause Menu", centered = true) {
    init {
        selection("Resume") {
            GameState.state = GameState.ACTIVE
        }

        selection("Save") {
            game.log.error("Saving not implemented!")
            // todo implement saving
            game.render(this)
        }

        selection("Settings") {
            game.render(SettingsMenu(game, this))
        }

        selection("Save and Exit Game") {
            // Save
            game.log.warn("Saving not implemented")
            // todo implement saving

            // Exit
            game.log.info("Exiting")
            game.window.dispose()
            Gdx.app.exit()
            SoundSystem.end()
            exitProcess(0)
        }
    }
}