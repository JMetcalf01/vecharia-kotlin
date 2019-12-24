package vecharia.menu

import vecharia.Vecharia
import vecharia.render.SoundSystem
import vecharia.settings.Settings

/**
 * Creates the settings menu with selections:
 *     Toggle Sound (toggles the music on/off)
 *     Toggle Fullscreen (toggles full screen -- note this feature is not working yet)
 *     Exit Settings (exits settings and goes back to the previous menu)
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @constructor makes the list of selections
 *
 * @param game the Vecharia game instance
 * @param pastMenu the menu the settings menu came from (either start or pause)
 */
class SettingsMenu(game: Vecharia, pastMenu: Menu) : Menu(game, "Settings", closeOnSelect = false, caret = false, centered = true) {
    init {
        selection("${getToggle(Settings.musicEnabled)}Sounds") {
            SoundSystem.toggleVolume()
            it.title = "${getToggle(Settings.musicEnabled)}Sounds"
        }

        selection("${getToggle(true)}Fullscreen") {
            Settings.fullscreen = !Settings.fullscreen
            it.title = "${getToggle(Settings.fullscreen)}Fullscreen"
            game.log.error("Fullscreen toggle only works for restarting!")
        }

        selection("    Exit Settings") {
            it.menu.closeOnSelect = true
            game.render(if (pastMenu is StartMenu) StartMenu(game) else PauseMenu(game))
        }
    }

    /**
     * Returns a string consisting of "[ ]" if the option is not enabled
     * and a string consisting of "[*]" if the option is enabled.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param enabled whether the setting is enabled
     * @return the string as described above
     */
    private fun getToggle(enabled: Boolean): String = "[${if (enabled) "*" else " "}] "
}
