package vecharia.render

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import vecharia.Vecharia
import vecharia.util.GameState
import vecharia.util.SimpleQueue
import vecharia.util.Tickable
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Prints text to the canvas.
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @constructor registers key listeners for skip and continue
 *
 * @param game the Vecharia game instance
 * @param canvas the canvas instance
 */
class Printer(game: Vecharia, private val canvas: Canvas) : Tickable {

    private val queue: SimpleQueue<Text> = SimpleQueue()
    private var waiting: AtomicBoolean = AtomicBoolean(false)

    init {
        game.addInputEvent(Input.Keys.ENTER) {
            waiting.set(false)
            queue.pop()?.callback?.invoke()
            canvas.clear()
        }
        game.addInputEvent(Input.Keys.SPACE) {
            if (GameState.state == GameState.ACTIVE) {
                queue.peek()?.instant = true
            }
        }
    }

    /**
     * Prints one or more characters of the next text in the queue,
     * taking into account text parameters.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see Text
     * @param game the Vecharia game instance
     * @param frame the current frame count of the game
     */
    override fun tick(game: Vecharia, frame: Int) {
        // Only runs every 4 frames
        if (frame % 4 != 0 || waiting.get())
            return

        val text: Text? = queue.peek()
        if (text != null) {

            // If you pass in an empty string, it clears
            if (text.message.isEmpty()) {
                canvas.clear()
                return
            }

            // Implements non-instant text
            if (!text.instant) {
                canvas.print(text.message[0].toString(), text.color)
                text.message = text.message.substring(1)
            } else {
                canvas.print(text.message, text.color)
                text.message = ""
            }

            // If empty, move to the next message or wait for user to continue
            if (text.message.isEmpty()) {
                if (text.newLine) {
                    canvas.println()
                }
                if (text.wait) {
                    canvas.println()
                    canvas.print("Hit Enter to Continue")
                    canvas.println()
                    waiting.set(true)
                } else {
                    queue.pop()
                    text.callback()
                }
            }
        }
    }

    /**
     * Adds text to the end of the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param message the message to be printed
     * @param color the color of the message
     * @param newLine whether to print a new line
     * @param wait whether to wait or not at the end of the line
     */
    fun print(message: String, color: Color = Color.WHITE, newLine: Boolean = true, wait: Boolean = false, instant: Boolean = false, callback: () -> Unit = {}) {
        queue.push(Text(message, color, newLine, wait, instant, callback))
    }

    /**
     * Adds text to the end of the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see Text
     *
     * @param text the text to be printed
     */
    fun print(text: Text) {
        queue.push(text)
    }

    /**
     * Adds an empty line to the end of the queue,
     * which the printer reads as clearing the canvas.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun clear() {
        queue.push(Text(""))
    }

    /**
     * Alternate way of adding text to the queue like so:
     * printer += (text)
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @param text the text to be added
     */
    operator fun plusAssign(text: Text) {
        queue.push(text)
    }
}

/**
 * A string of text to be printed.
 */
data class Text(var message: String, val color: Color = Color.WHITE, val newLine: Boolean = true, val wait: Boolean = false, var instant: Boolean = false, val callback: () -> Unit = {}) {
    override fun toString(): String {
        return message
    }
}