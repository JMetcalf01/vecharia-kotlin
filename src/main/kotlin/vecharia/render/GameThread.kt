package vecharia.render

import com.badlogic.gdx.Gdx
import java.lang.Exception

class GameThread(val win: Window) : Thread() {

    init {
        this.isDaemon = true
        this.start()
    }

    override fun run() {
        println("Starting the game thread...")

        //Start the game here
        //todo

        // Exits game
        Gdx.app.exit()
    }

    fun sleep(milis: Int) {
        try {
            this.sleep(milis)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pause() {
        try {
            while (win.entering) {
                this.sleep(10)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}