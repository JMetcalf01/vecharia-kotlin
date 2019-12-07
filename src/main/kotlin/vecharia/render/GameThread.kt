package vecharia.render

import com.badlogic.gdx.Gdx
import java.lang.Exception

class GameThread(private val win: Window) : Thread() {

    init {
        this.isDaemon = true
        this.start()
    }

    override fun run() {
        println("Starting the game thread...")

        //Start the game here
        //todo
        while (true) {

        }

        // Exits game
        Gdx.app.exit()
    }

    /**
     *
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
     *
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