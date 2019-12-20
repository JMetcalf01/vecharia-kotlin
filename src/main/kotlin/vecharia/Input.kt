package vecharia

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import vecharia.render.Text
import vecharia.util.GameState
import vecharia.util.Promise
import vecharia.util.State
import vecharia.util.Tickable
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CopyOnWriteArrayList

/**
 * A universal input object for use throughout the game.
 *
 * @author Matt Worzala
 * @since 1.2
 */
object Input : Tickable {
    /**
     * A queue of keys being pressed, this contains the GDX id of the key pressed.
     */
    private val queue: ConcurrentLinkedQueue<Int> = ConcurrentLinkedQueue()
    private val events: MutableMap<State, MutableMap<Int, MutableList<Triple<Int, Boolean, () -> Unit>>>> = mutableMapOf()

    private val buffer: StringBuilder = StringBuilder()
    private var cb: ((String) -> Unit)? = null

    var typing: Boolean = false
        private set
    private var min: Int = -1
    private var max: Int = -1

    val current get() = buffer.toString()
    val length get() = buffer.length

    /**
     * Add all recently pressed keys to the user input buffer, if the user is currently being prompted for input.
     * Run the GameState respecting key event for all keys pressed in the last frame.
     *
     * @author Matt Worzala
     * @since 1.2
     *
     * @see vecharia.util.GameState
     * @param game The Vecharia game instance
     * @param frame the current frame
     */
    override fun tick(game: Vecharia, frame: Long) {
        while (queue.size > 0) {
            val raw = queue.poll()
            val shift = raw - 1000 > 0
            val key = if (shift) raw - 1000 else raw

            // Add input to typing buffer
            if (typing && GameState.state != GameState.PAUSED) {
                if (max == -1 || current.trim().length < max) {
                    if ((A..Z).contains(key))
                        buffer.append((if (shift) key + 36 else key + 68).toChar())

                    if ((NUM_0..NUM_9).contains(key))
                        buffer.append((key + 41).toChar())

                    if (key == SPACE && !current.endsWith(' ')) buffer.append(' ')
                    if (key == MINUS) buffer.append('-')
                    if (key == PERIOD) buffer.append('.')
                }

                if (key == BACKSPACE && buffer.isNotEmpty())
                    buffer.setLength(buffer.length - 1)

                if (key == ENTER && (min == -1 || current.trim().length >= min)) {
                    typing = false
                    game.printer += Text("> $current", instant = true)
                    cb?.invoke(current.trim())
                    buffer.clear()
                }
            }

            // Call key events
            val events: List<Triple<Int, Boolean, () -> Unit>> = events[GameState.state]?.get(key) ?: mutableListOf()
            for (event in events) {
                event.third()
                if (event.second) this -= event.first
            }

        }
    }

    /**
     * Prompt the user for input and make use of the response.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param max the max number of characters acceptable in the input
     * @return a promise of the text entered by the user
     */
    fun readInput(minLength: Int = -1, maxLength: Int = -1): Promise<String> {
        if (typing) throw IllegalStateException("Already reading input!")
        typing = true
        min = minLength
        max = maxLength
        return Promise { cb = it }
    }

    /**
     * Register a new key event for the given key, in the given game state.
     *
     * @author Matt Worzala
     * @since 1.2
     *
     * @see com.badlogic.gdx.Input.Keys
     * @param key the key to listen for
     * @param state the game state to listen for
     * @param once whether to remove the event after a single hit
     * @param callback the callback to invoke when the key is detected
     * @return the id of the listener, this is required to remove the listener manually.
     */
    fun registerListener(key: Int, state: State = GameState.ACTIVE, once: Boolean = false, callback: () -> Unit): Int {
        val id = (10000..99999).random()
        events.getOrPut(state, { mutableMapOf() }).getOrPut(key, { CopyOnWriteArrayList() }).add(Triple(id, once, callback))
        return id
    }

    /**
     * Remove a listener by it's unique id.
     *
     * @author Matt Worzala
     * @since 1.2
     *
     * @param id the id of the listener to remove
     */
    fun removeListener(id: Int) = events.values.forEach { keys -> keys.values.forEach { actions -> actions.removeIf { it.first == id } } }

    /**
     * An operator alternative to remove an event listener.
     *
     * @author Matt Worzala
     * @since 1.2
     *
     * @param id the id of the listener to remove
     */
    operator fun minusAssign(id: Int) = removeListener(id)

    /**
     * An internal function used to read every key on the keyboard each frame.
     * This should be called from Window#render only.
     *
     * @author Matt Worzala
     * @since 1.2
     */
    internal fun readGdxInput() {
        val shift = Gdx.input.isKeyPressed(SHIFT_LEFT) || Gdx.input.isKeyPressed(SHIFT_RIGHT)
        (NUM_0..NUMPAD_9).filter { Gdx.input.isKeyJustPressed(it) }.forEach { queue.offer(if (shift) it + 1000 else it) }
    }
}

