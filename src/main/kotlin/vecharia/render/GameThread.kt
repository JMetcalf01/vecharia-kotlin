package vecharia.render

import vecharia.Vecharia
import vecharia.menu.StartMenu
import vecharia.util.GameState
import java.lang.Exception

/**
 * This class is for the thread that the game runs on.
 *
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @constructor starts the thread
 *
 * @param game the Vecharia game instance
 */
class GameThread(private val game: Vecharia) : Thread() {

    init {
        this.isDaemon = true
        this.start()
    }

    /**
     * Starts the thread, begins the main menu.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    override fun run() {
        game.log.info("Game thread started...")
        GameState.state = GameState.ACTIVE

//        SoundSystem.add("assets/introscreen.mp3", looping = true, volume = 0f)
//        SoundSystem.playM()
//        game.log.info("Sound system playing")

        val startMenu = StartMenu(game)
        game.log.info("Entering Start Menu")
        game.render(startMenu)
        while (true) {
            sleep(10)
        }
    }

    /**
     * Sleeps the thread while the user is entering.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun getInput() {
        try {
            while (game.isTyping()) {
                sleep(10)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sleeps the Game Thread for a set number of milliseconds.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param millis the amount of milliseconds to sleep
     */
    private fun sleep(millis: Int) {
        try {
            sleep(millis.toLong())
        } catch (ignored: Exception) {
        }
    }
}