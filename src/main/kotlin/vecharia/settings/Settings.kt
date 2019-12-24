package vecharia.settings

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

/**
 * The settings that the player has access to.
 *
 * @author Jonathan Metcalf
 * @since 1.4
 */
object Settings {

    var fullscreen: Boolean = true
        set(value) {
            field = value
            save()
        }

    var musicEnabled: Boolean = true
        set(value) {
            field = value
            save()
        }

    /**
     * Saves the current settings choices into settings.json.
     * Note this is called whenever a value is set.
     *
     * @author Jonathan Metcalf
     * @since 1.4
     */
    fun save() {
        val settings = SettingsData(fullscreen, musicEnabled)
        val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
        val jsonSettings: String = gsonBuilder.toJson(settings)
        File("settings/settings.json").writeText(jsonSettings)
    }

    /**
     * Loads the current settings from the existing settings.json file.
     * This should be done when the game is first loaded.
     *
     * @author Jonathan Metcalf
     * @since 1.4
     */
    fun load() {
        val jsonSettings: String = File("settings/settings.json").readText()
        val settingsData = Gson().fromJson(jsonSettings, SettingsData::class.java)
        fullscreen = settingsData.fullscreen
        musicEnabled = settingsData.musicEnabled
    }
}

/**
 * A data class that matches with the settings options for use in creating the JSON string to print to settings.json
 *
 * @author Jonathan Metcalf
 * @since 1.4
 *
 * @param fullscreen whether the screen is fullscreen
 * @param musicEnabled whether the music is enabled
 */
data class SettingsData(val fullscreen: Boolean, val musicEnabled: Boolean)