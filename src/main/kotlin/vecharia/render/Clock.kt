@file:Suppress("SpellCheckingInspection")

package vecharia.render

import vecharia.Vecharia
import vecharia.util.GameState
import vecharia.util.Tickable
import kotlin.collections.HashMap

/**
 * The main clock in Vecharia. This allows for main menu
 * and pause menu access.
 *
 * @author Matt Worzala
 * @since 1.1
 *
 * @constructor starts the clock thread
 *
 * @param game the Vecharia game instance
 * @param speed the amount of milliseconds before the next frame
 */
class Clock(private val game: Vecharia, private val speed: Long = 5) : Thread() {

    private val tickables: MutableMap<vecharia.util.State, Tickable> = HashMap()

    init {
        this.isDaemon = true
        this.name = "Vecharia Clock"
        this.start()
    }

    /**
     * Iterates through every tickable in tickables
     * and ticks them if they match the gamestate.
     * This is the game loop.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @throws StackOverflowError if you leave your game open for too long
     */
    override fun run() {
        var frame = 0L
        while (true) {
            if (frame > Long.MAX_VALUE - 1_000_000) throw StackOverflowError("You probably left your game open...")

            sleep(speed)

            updateGame(frame)
            renderGame(frame)

            frame++
        }
    }


    /**
     * Ticks every tickable that should be ticked.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see GameState
     * @param frame the current frame of the game
     */
    private fun updateGame(frame: Long) {
        for ((state, tickable) in tickables) {
            if (state == GameState.state)
                tickable.tick(game, frame)
        }
    }

    /**
     * Does all the printing and rendering of the game.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @param frame the current frame of the game
     */
    private fun renderGame(frame: Long) {
        if (GameState.state == GameState.ACTIVE || GameState.state == GameState.UNLOADED)
            game.printer.tick(game, frame)
        else if (GameState.state == GameState.PAUSED)
            game.pauseMenu.tick(game, frame)

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