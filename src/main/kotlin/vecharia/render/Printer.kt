package vecharia.render

import com.badlogic.gdx.Input.Keys.ENTER
import com.badlogic.gdx.Input.Keys.SPACE
import com.badlogic.gdx.graphics.Color
import vecharia.Input
import vecharia.Vecharia
import vecharia.util.*

/**
 * Prints text to the canvas.
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @constructor registers key listeners for skip and continue
 *
 * @param canvas the canvas instance
 * @param state the state the printer prints in
 */
class Printer(private val canvas: Canvas, state: State) : Tickable {
    private val queue: SimpleQueue<Text> = SimpleQueue()
    private var waiting: Boolean = false

    val printing: Boolean get() = queue.peek() != null

    init {
        Input.registerListener(ENTER, state) {
            if (waiting) {
                waiting = false
                queue.pop()?.callback?.invoke()
                canvas.clear()
            }
        }
        Input.registerListener(SPACE, state) {
            if (GameState.state == GameState.ACTIVE)
                queue.peek()?.instant = true
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
    override fun tick(game: Vecharia, frame: Long) {
        // If waiting for user input, don't keep printing
        if (waiting)
            return

        val text: Text? = queue.peek()
        if (text != null) {

            // Slows down text to every 20 ms if it's not instant text
            if (frame % 4 != 0L && !text.instant)
                return

            // If you pass in an empty string, it clears
            if (text.message.isEmpty()) {
                canvas.clear()
                queue.pop()
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
                    waiting = true
                } else {
                    queue.pop()
                    text.callback()
                    tick(game, frame)
                }
            }
        }
    }

    /**
     * Adds text to the end of the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @param message the message to be printed
     * @param color the color of the message
     * @param newLine whether to print a new line
     * @param wait whether to wait or not at the end of the line
     * @param instant whether the line prints instantly
     * @param callback the event to call after printing
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
     * @see SimpleQueue
     * @param text the text to be printed
     */
    private fun print(text: Text) {
        queue.push(text)
    }

    /**
     * Queues a text with the waiting flag set to true and returns a promise of the wait completion.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param message the message of the text
     * @param color the color of the text
     * @param newLine whether to print a new line
     * @return a promise that it will print the text
     */
    fun waiting(message: String, color: Color = Color.WHITE, newLine: Boolean = true): Promise<Unit> = Promise {
        print(Text(message, color, newLine, wait = true) { it(Unit) })
    }

    /**
     * Queues an arbitrary amount of strings.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param messages the list of messages to print
     * @param clear whether the clear beforehand
     */
    fun batch(vararg messages: String, clear: Boolean = true) {
        if (clear) clear()
        messages.forEach { this += it }
    }

    /**
     * Adds an empty line to the end of the queue,
     * which the printer reads as clearing the canvas.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see SimpleQueue
     */
    fun clear() {
        queue.push(Text("", instant = true))
    }

    /**
     * Alternate way of adding text to the queue like so:
     * printer += (text)
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see SimpleQueue
     * @param text the text to be added
     */
    operator fun plusAssign(text: Text) {
        queue.push(text)
    }

    /**
     * Alternate way of adding text to the queue like so:
     * printer += (text)
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see SimpleQueue
     * @param string the text to be added
     */
    operator fun plusAssign(string: String) {
        queue.push(Text(string))
    }
}

/**
 * A string of text to be printed.
 *
 * @author Jonathan Metcalf
 * @since 1.1
 *
 * @param message the string of text
 * @param color the color of the text
 * @param newLine whether to print a new line after
 * @param wait whether to wait for user input after printing
 * @param instant whether to print the line instantly
 * @param callback the event to be done after printing is finished
 */
data class Text(var message: String, val color: Color = Color.WHITE, val newLine: Boolean = true, val wait: Boolean = false, var instant: Boolean = false, val callback: () -> Unit = {}) {
    override fun toString(): String {
        return message
    }
}