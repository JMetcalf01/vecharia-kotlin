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
 * The entry point into the program
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

class Vecharia(val log: Logger, private val window: Window) {
    lateinit var gameThread: GameThread

    private val skipPrint = AtomicBoolean(false)

    fun start() {
        gameThread = GameThread(this)
        window.addKeyAction(Input.Keys.SPACE) {
            println("Hello ${isTyping()}")
            if (!window.entering) skipPrint.set(true)
        }
    }

    fun isTyping(): Boolean = window.entering

    fun getTextInput(): String = window.readLine()

    fun getMenuInput(menu: Menu) = menu.render(this)

    fun addInputEvent(key: Int, onInput: () -> Unit) = window.addKeyAction(key, onInput)

    fun removeInputEvent(key: Int) = window.removeKeyAction(key)

    fun clear() = window.canvas.clear()

    fun print(message: String, color: Color = Color.WHITE, delay: Long = 20, newLine: Boolean = true, wait: Boolean = false) {
        skipPrint.set(false)
        for (char in message) {
            window.canvas.print(char.toString(), color)
            if (!skipPrint.get())
                sleep(delay)
        }
        if (newLine) window.canvas.println()
        skipPrint.set(false)
    }

    fun sleep(length: Long) {
        try {
            Thread.sleep(length)
        } catch (ignore: Exception) { }
    }
}