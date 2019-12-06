package desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import vecharia.render.Window
import java.awt.Toolkit

fun main() {
    val config = LwjglApplicationConfiguration()

    config.height = Toolkit.getDefaultToolkit().screenSize.height
    config.width = Toolkit.getDefaultToolkit().screenSize.width
    config.fullscreen = true
    config.resizable = false

    LwjglApplication(Window(), config)
}