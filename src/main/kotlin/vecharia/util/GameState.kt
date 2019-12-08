package vecharia.util

import java.util.concurrent.atomic.AtomicReference

object GameState {
    val UNLOADED: State = State(0)
    val ACTIVE: State = State(1)
    val PAUSED: State = State(2)

    private val safeState: AtomicReference<State> = AtomicReference(UNLOADED)
    var state: State
        get() = safeState.get()
        set(value) = safeState.set(value)
}

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