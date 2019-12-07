package vecharia

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import vecharia.logging.Logger
import vecharia.menu.MainMenu
import vecharia.render.GameThread
import vecharia.render.Window
import vecharia.menu.Menu
import vecharia.render.Clock
import vecharia.util.Tickable
import java.awt.Toolkit
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

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
class Vecharia(val log: Logger, val window: Window) {
    lateinit var gameThread: GameThread
    private lateinit var clock: Clock
    private val ticking: MutableMap<Int, Tickable> = HashMap()

    private var printJob: PrintJob? = null

    /**
     * Starts the game thread, and adds a keybind to speed up text
     * if space is hit.
     */
    fun start() {
        clock = Clock(this)
        gameThread = GameThread(this)
        window.addKeyAction(Input.Keys.SPACE) {
            if (!window.entering) printJob?.skipped = true
        }
        MainMenu.register(this)
    }

    fun prompt(menu: Menu): Int {
        menu.render(this)
        val id: Int
        synchronized(ticking) {
            id = startTicking(menu)
        }
        while (!menu.ready)
            sleep(5)
        synchronized(ticking) {
            stopTicking(id)
        }
        return menu.selection
    }

    fun tick(frame: Int) {
        if (frame % 4 == 0) {
            tickPrint()
        }
        synchronized(ticking) {
            for (ticker in ticking.values)
                ticker.tick(this)
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

    fun startTicking(tickable: Tickable): Int {
        ticking[tickable.hashCode()] = tickable
        return tickable.hashCode()
    }

    fun stopTicking(id: Int) {
        ticking.remove(id)
    }

    /**
     * Adds a keybind for a specific action.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key to trigger the action
     * @param onInput the action to be performed
     */
    fun addInputEvent(key: Int, paused: Boolean = false, onInput: () -> Unit) =
        window.addKeyAction(key, paused, onInput)

    /**
     * Removes a specific keybind.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param key the key for the keybind to remove
     */
    fun removeInputEvent(key: Int, paused: Boolean = false) = window.removeKeyAction(key, paused)

    /**
     * Clears the canvas.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    fun clear() = window.canvas.clear()

    private fun tickPrint() {
        val job = printJob
        if (job != null) {
            if (job.skipped || job.delay == 0L) {
                window.canvas.print(job.message, job.color)
                window.canvas.println()
                printJob = null
            } else {
                window.canvas.print(job.message[0].toString(), job.color)
                if (job.message.length == 1) {
                    window.canvas.println()
                    printJob = null
                } else printJob?.message = job.message.substring(1)
            }
        }
    }

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
        printJob = PrintJob(message, color, delay, newLine, wait)
        while (this.printJob != null) {
            sleep(5)
        }
    }

    /**
     * Sleeps all threads for the inputted amount of time.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param length the time to sleep the thread
     */
    private fun sleep(length: Long) {
        try {
            Thread.sleep(length)
        } catch (ignore: Exception) {
        }
    }

    class PrintJob(msg: String, val color: Color = Color.WHITE, val delay: Long = 20, val newLine: Boolean = true, val wait: Boolean = false) {
        private val safeMessage: AtomicReference<String> = AtomicReference(msg)
        var message: String
            get() = safeMessage.get()
            set(value) = safeMessage.set(value)
        private val safeSkip: AtomicBoolean = AtomicBoolean(false)
        var skipped: Boolean
            get() = safeSkip.get()
            set(value) = safeSkip.set(value)
    }
}