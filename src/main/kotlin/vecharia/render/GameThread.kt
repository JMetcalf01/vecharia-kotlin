package vecharia.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import vecharia.Vecharia
import vecharia.menu.MainMenu
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
        this.name = "Vecharia Game"
        this.start()
    }

    /**
     * Starts the thread.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    override fun run() {
        game.log.info("Game Thread started")

        val menu = Menu("Super Cool Test Menu")
        menu.selection("Selection 1") {
            println("Selection 1 was selected.")
        }
        menu.selection("Selection 2") {
            println("Selection 2 was selected.")
        }
        menu.selection("Selection 3") {
            println("Selection 3 was selected.")
        }
        menu.selection("Selection 4") {
            println("Selection 4 was selected.")
        }

        while (true) {

            println(game.prompt(menu))
        }

        // Exits game
        Gdx.app.exit()
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