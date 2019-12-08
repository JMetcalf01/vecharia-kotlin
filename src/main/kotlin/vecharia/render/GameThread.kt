package vecharia.render

import com.badlogic.gdx.Gdx
import vecharia.Vecharia
import vecharia.menu.Menu
import java.lang.Exception

/**
 * This class is for the thread that the game runs on.
 *
 * @constructor starts the thread
 *
 * @author Jonathan Metcalf
 * @since 1.0
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

        SoundSystem.add("assets/introscreen.mp3", looping = true)
        SoundSystem.playM()
        game.log.info("Sound system playing")


        val menu = Menu("Cool menu title", centered = true)

        menu.selection("New Game") {
            game.log.info("Starting New Game...")
        }

        menu.selection("Load Game") {
            game.log.warn("Load Game In Progress!")
        }

        menu.selection("Settings") {
            game.log.info("Settings...")
        }

        menu.selection("Exit") {
            game.log.info("Exiting...")
            Gdx.app.exit()
        }




        while (true) {
//            game.print(game.getTextInput(), wait = true)
            game.getMenuInput(menu)
//            game.print(game.getTextInput())
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
                this.sleep(10)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}