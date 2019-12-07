package vecharia.render

import com.badlogic.gdx.Gdx
import vecharia.Vecharia
import vecharia.util.Menu
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
     * Starts the thread.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    override fun run() {
        println("Starting the game thread...")

        //Start the game here (todo)



        val menu = Menu("Cool menu title")

        menu.selection("Option 1") {
            println("Option 1 was selected")
        }

        menu.selection("Option 2") {
            println("Option 2 was selected")
        }

        menu.selection("Option 3") {
            println("Option 3 was selected")
        }




        while (true) {
            game.getMenuInput(menu)
//            game.print(game.getTextInput())
            sleep(10)
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