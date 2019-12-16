package vecharia.menu

import vecharia.Vecharia
import vecharia.render.SoundSystem

/**
 * Creates the settings menu with selections:
 *     Toggle Sound (toggles the music on/off)
 *     Toggle Fullscreen (toggles full screen -- note this feature is not working yet)
 *     Exit Settings (exits settings and goes back to the previous menu)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @param game the Vecharia game instance
 * @param pastMenu the menu the settings menu came from (either start or pause)
 */
class SettingsMenu(game: Vecharia, pastMenu: Menu) : Menu(game, "Settings", centered = true) {
    init {
        selection("Toggle Sound (Currently ${if (SoundSystem.musicEnabled) "Enabled" else "Disabled"})") {
            SoundSystem.toggleVolume()
            game.log.info("Music set to ${if (SoundSystem.musicEnabled) "on" else "off"}")
            game.render(SettingsMenu(game, pastMenu))
        }

        selection("Toggle Fullscreen") {
            game.log.error("Fullscreen not implemented!")
            // Todo implement fullscreen
        }

        // this is temporary until we set up pause menu
        selection("temp pause menu access") {
            game.render(PauseMenu(game))
        }

        selection("Exit Settings") {
            game.log.info("Exiting settings")
            game.render(if (pastMenu is StartMenu) StartMenu(game) else PauseMenu(game))
        }
    }
}
