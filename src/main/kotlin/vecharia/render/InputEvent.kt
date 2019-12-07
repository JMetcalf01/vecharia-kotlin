package vecharia.render

/**
 * Represents a key input event.
 */
@FunctionalInterface
interface InputEvent {
    /**
     * Called when a user presses the key assigned to the event.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    fun onInput()
}