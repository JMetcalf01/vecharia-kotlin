package vecharia.util

import java.util.concurrent.atomic.AtomicReference

/**
 * A singleton that is the state of the game:
 *     Unloaded if the game is still in the main menu,
 *     Active is the game is currently being played
 *     Paused if the game has been paused
 *
 * @author Matt Worzala
 * @since 1.1
 */
object GameState {
    val UNLOADED: State = State(0)
    val ACTIVE: State = State(1)
    val PAUSED: State = State(2)

    private val safeState: AtomicReference<State> = AtomicReference(UNLOADED)
    var state: State
        get() = safeState.get()
        set(value) = safeState.set(value)
}

/**
 * Represents a game state.
 *
 * @author Matt Worzala
 * @since 1.1
 *
 * @param id the id of the state
 */
data class State(private val id: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as State
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id
    }
}