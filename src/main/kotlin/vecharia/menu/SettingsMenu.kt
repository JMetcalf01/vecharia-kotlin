package vecharia.menu

import vecharia.Vecharia
import vecharia.render.SoundSystem

/**
 * Creates the settings menu with selections:
 *     New Game (creates new game)
 *     Load Game (loads game from save file)
 *     Settings (opens settings menu)
 *     Credits (runs the credits)
 *     Exit (exits the game)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 */
class SettingsMenu(game: Vecharia, pastMenu: Menu) : Menu(game, "Settings Menu", centered = true) {
    init {
        selection("Toggle Sound (Currently ${if (SoundSystem.musicEnabled) "Enabled" else "Disabled"})") {
            game.log.info("Music set to ${if (SoundSystem.musicEnabled) "on" else "off"}")
            SoundSystem.toggleVolume()
        }

        selection("Toggle Fullscreen") {
            game.log.error("Fullscreen not implemented!")
            // Todo implement fullscreen
        }

        selection("Exit Settings") {
            game.log.info("Exiting settings")
            game.render(if (pastMenu is StartMenu) StartMenu(game) else PauseMenu(game))
        }
    }
}
