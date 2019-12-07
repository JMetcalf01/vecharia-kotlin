package vecharia

import com.badlogic.gdx.Input
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import vecharia.logging.Logger
import vecharia.render.GameThread
import vecharia.render.Window
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
            if (!window.entering) skipPrint.set(true)
        }
    }

    fun isTyping(): Boolean = window.entering

    fun getTextInput(): String = window.readLine()

    fun clear() = window.canvas.clear()

    fun print(message: String, wait: Boolean = false) {
        skipPrint.set(false)
        for (char in message) {
            window.canvas.print(char)
            if (!skipPrint.get())
                sleep(20)
        }
        skipPrint.set(false)
    }

    private fun sleep(length: Long) {
        try {
            Thread.sleep(length)
        } catch (ignore: Exception) { }
    }
}