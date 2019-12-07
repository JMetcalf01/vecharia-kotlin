package vecharia.util

import java.util.concurrent.atomic.AtomicBoolean

object GameState {
    private val pauseActions: MutableList<() -> Unit> = mutableListOf()
    private val safePaused: AtomicBoolean = AtomicBoolean(false)
    var paused: Boolean
        get() = safePaused.get()
        set(value) {
            safePaused.set(value)
            if (value) pauseActions.forEach { it() }
        }

    fun onPause(func: () -> Unit) = pauseActions.add(func)

    val running: AtomicBoolean = AtomicBoolean(true)
}