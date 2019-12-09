package vecharia.render

import com.badlogic.gdx.Gdx
import vecharia.Vecharia
import vecharia.menu.Menu
import vecharia.util.GameState
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
        GameState.state = GameState.ACTIVE

//        SoundSystem.add("assets/introscreen.mp3", looping = true)
//        SoundSystem.playM()
//        game.log.info("Sound system playing")

        val menu = startMenu()
        game.log.info("Menu created")

        while (true) {
            game.getMenuInput(menu)
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
        } catch (ignored: Exception) {
        }
    }

    /**
     * Creates the start menu with selections:
     *     New Game (creates new game)
     *     Load Game (loads game from save file)
     *     Settings (opens settings menu)
     *     Credits (runs the credits)
     *     Exit (exits the game)
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @return the start menu
     */
    private fun startMenu(): Menu {
        val menu = Menu("Welcome to Vecharia!", centered = true)

        menu.selection("New Game") {
            game.log.info("New game started")
        }

        menu.selection("Load Game") {
            game.log.warn("Load game in progress!")
        }

        menu.selection("Settings") {
            game.log.info("Settings opened")
        }

        menu.selection("Credits") {
            game.log.info("Running credits")
        }

        menu.selection("Exit") {
            game.log.info("Exiting")
            Gdx.app.exit()
        }

        return menu
    }

}