package vecharia.render

import com.badlogic.gdx.Gdx
import java.lang.Exception

/**
 * This class is for the thread that the game runs on
 *
 * @constructor starts the thread
 *
 * @author Jonathan Metcalf
 * @since Version 1.0
 */
class GameThread(private val win: Window) : Thread() {

    init {
        this.isDaemon = true
        this.start()
    }

    /**
     * Starts the thread
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     */
    override fun run() {
        println("Starting the game thread...")

        //Start the game here (todo)

        while (true) {
            win.canvas.println(win.readLine())
            sleep(10)
        }

        // Exits game
        Gdx.app.exit()
    }

    /**
     * Sleeps the thread while the user is entering
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     */
    fun getInput() {
        try {
            while (win.entering) {
                this.sleep(10)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Sleeps the Game Thread for a set number of milliseconds
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     *
     * @param millis the amount of milliseconds to sleep
     */
    private fun sleep(millis: Int) {
        try {
            this.sleep(millis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}