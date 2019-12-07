package vecharia.render

import vecharia.Vecharia
import vecharia.menu.MainMenu
import vecharia.util.GameState

class Clock(private val game: Vecharia, private val speed: Long = 5) : Thread() {
    init {
        this.isDaemon = true
        this.name = "Vecharia Clock"
        this.start()
    }

    override fun run() {
        var frame = 0
        while (GameState.running.get()) {
            if (frame % 10 == 0)
                println("heartbeat")
            if (GameState.paused)
                MainMenu.tick(frame)
            else game.tick(frame)



            sleep(speed)
            frame++
        }
    }
}