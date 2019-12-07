package vecharia

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import vecharia.logging.Logger
import vecharia.render.GameThread
import vecharia.render.Window
import vecharia.util.Menu
import java.awt.Toolkit
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

/**
 * The entry point into the program.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
fun main() {
    val config = LwjglApplicationConfiguration()

    config.height = Toolkit.getDefaultToolkit().screenSize.height
    config.width = Toolkit.getDefaultToolkit().screenSize.width
    config.fullscreen = true
    config.resizable = false

    LwjglApplication(Window(), config)
}

/**
 * The main game class.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
class Vecharia(val log: Logger, private val window: Window) {
    lateinit var gameThread: GameThread

    private val skipPrint = AtomicBoolean(false)

    /**
     * Starts the game thread, and adds a keybind to speed up text
     * if space is hit.
     */
    fun start() {
        gameThread = GameThread(this)
        window.addKeyAction(Input.Keys.SPACE) {
            println("Hello ${isTyping()}")
            if (!window.entering) skipPrint.set(true)
        }
    }

    /**
     * Returns whether the user is currently typing.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return whether the user is typing
     */
    fun isTyping(): Boolean = window.entering

    /**
     * Read a single line of input from the user.
     * This is a thread blocking action, it will wait until the line has been submitted.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the string the user typed
     */
    fun getTextInput(): String = window.readLine()

    fun getMenuInput(menu: Menu) = menu.render(this)

    /**
     * Adds a keybind for a specific action.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key to trigger the action
     * @param onInput the action to be performed
     */
    fun addInputEvent(key: Int, onInput: () -> Unit) = window.addKeyAction(key, onInput)

    /**
     * Removes a specific keybind.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key for the keybind to remove
     */
    fun removeInputEvent(key: Int) = window.removeKeyAction(key)

    /**
     * Clears the canvas.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    fun clear() = window.canvas.clear()

    /**
     * Prints a line of text in a specific color, with a possible delay between each letter (so it scrolls),
     * as well as ending it with a new line or waiting at the end until the user hits ENTER.
     *
     * @author Matt Worzala and Jonathan Metcalf
     * @since 1.0
     *
     * @param message the message to be printed
     * @param color the color of the printed text (defaults to white)
     * @param delay the delay between each printed letter (defaults to 20)
     * @param newLine whether to print a new line after (defaults to true)
     * @param wait whether to wait after it finishes printing (defaults to false)
     */
    fun print(
        message: String,
        color: Color = Color.WHITE,
        delay: Long = 20,
        newLine: Boolean = true,
        wait: Boolean = false
    ) {
        skipPrint.set(false)
        for (char in message) {
            window.canvas.print(char.toString(), color)
            if (!skipPrint.get())
                sleep(delay)
        }
        if (wait) {
            window.entering = true
            window.canvas.println()
            window.canvas.print("Hit enter to continue.")
            window.canvas.println()
            window.addKeyAction(Input.Keys.ENTER) {
                window.entering = false
                clear()
            }
            while (window.entering) sleep(5)
            window.removeKeyAction(Input.Keys.ENTER)
        } else if (newLine) window.canvas.println()
        skipPrint.set(false)
    }

    /**
     * Sleeps all threads for the inputted amount of time.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param length the time to sleep the thread
     */
    fun sleep(length: Long) {
        try {
            Thread.sleep(length)
        } catch (ignore: Exception) {
        }
    }
}