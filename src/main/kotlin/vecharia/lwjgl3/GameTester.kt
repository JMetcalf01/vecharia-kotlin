package vecharia.lwjgl3

import org.lwjgl.opengl.GL11
import vecharia.lwjgl3.font.FontType
import vecharia.lwjgl3.font.GUIText
import vecharia.lwjgl3.render.Texture
import vecharia.lwjgl3.util.Color
import vecharia.lwjgl3.util.Position
import java.io.File

fun main() {
    val engine: GameEngine = GameEngine.create(true)

    val candara = FontType(engine.window, Texture.load("/home/matt/dev/kotlin/vecharia/assets/candara.png").id, File("/home/matt/dev/kotlin/vecharia/assets/candara.fnt"))
    val text = GUIText("Text 2: Electric Boogaloo", 4f, candara, Position(y = 0.5f), 1.0f, true)

    var color = 0
    var red = 1f
    var green = 0f
    var blue = 0f

    while (engine.isRunning()) {
        engine.update()

        GL11.glClearColor(0f, 0f, 0f, 1f)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)

        val delta = 0.005f
        when (color) {
            0 -> {
                red += delta
                if (blue > 0f)
                    blue -= delta
                if (red > 1f)
                    color = 1
            }
            1 -> {
                green += delta
                if (red > 0f)
                    red -= delta
                if (green > 1f)
                    color = 2
            }
            2 -> {
                blue += delta
                if (green > 0f)
                    green -= delta
                if (blue > 1f)
                    color = 0
            }
        }
        text.color = Color(red, green, blue)

        engine.render()

        engine.tick()
    }
}