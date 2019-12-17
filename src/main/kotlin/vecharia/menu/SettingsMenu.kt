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
class SettingsMenu(game: Vecharia, pastMenu: Menu) : Menu(game, "Settings", closeOnSelect = false, caret = false, centered = true) {
    init {
        selection("${getToggle(SoundSystem.musicEnabled)}Sounds") {
            SoundSystem.toggleVolume()
            game.log.info("Music set to ${if (SoundSystem.musicEnabled) "on" else "off"}")
            it.title = "${getToggle(SoundSystem.musicEnabled)}Sounds"
        }

        selection("${getToggle(true)}Fullscreen") {
            game.log.error("Fullscreen not implemented!")
            // Todo implement fullscreen
        }

        // this is temporary until we set up pause menu
        selection("    temp pause menu access") {
            it.menu.closeOnSelect = true
            game.render(PauseMenu(game))
        }

        selection("    Exit Settings") {
            game.log.info("Exiting settings")
            it.menu.closeOnSelect = true
            game.render(if (pastMenu is StartMenu) StartMenu(game) else PauseMenu(game))
        }
    }

    private fun getToggle(enabled: Boolean): String = "[${if (enabled) "*" else " "}] "
}
