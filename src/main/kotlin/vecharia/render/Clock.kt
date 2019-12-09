package vecharia.render

import vecharia.Vecharia
import vecharia.util.GameState
import vecharia.util.Tickable
import kotlin.collections.HashMap

/**
 * The main clock in Vecharia. This allows for main menu
 * and pause menu access.
 *
 * @constructor starts the clock thread
 *
 * @author Matt Worzala
 * @since 1.1
 */
class Clock(private val game: Vecharia, private val speed: Long = 5) : Thread() {

    val tickables: MutableMap<vecharia.util.State, Tickable> = HashMap()

    init {
        this.isDaemon = true
        this.name = "Vecharia Clock"
        this.start()
    }

    /**
     * Iterates through every tickable in tickables
     * and ticks them if they match the gamestate.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    override fun run() {
        var frame = 0
        while (true) {
            if (frame > 2_000_000_000) throw StackOverflowError("Close your fucking game you reject")


            if (GameState.state == GameState.ACTIVE || GameState.state == GameState.UNLOADED) game.printer.tick(game, frame)
            // else todo implement paused printer

            for ((state, tickable) in tickables) {
                if (state == GameState.state)
                    tickable.tick(game, frame)
            }
            sleep(speed)
            frame++
        }
    }

    /**
     * Sets a specific instance of the state to the tickable.
     *
     * @author Matt Worzala
     * @since 1.1
     *
     * @param state the game state
     * @param value the tickable
     */
    operator fun set(state: vecharia.util.State, value: Tickable) {
        tickables[state] = value
    }
}