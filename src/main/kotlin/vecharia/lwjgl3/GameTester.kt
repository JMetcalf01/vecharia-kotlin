package vecharia.lwjgl3

import glm_.vec4.Vec4
import imgui.ImGui
import imgui.WindowFlag
import org.lwjgl.opengl.GL11
import vecharia.lwjgl3.font.FontType
import vecharia.lwjgl3.font.GUIText
import vecharia.lwjgl3.render.Texture
import vecharia.lwjgl3.util.Color
import vecharia.lwjgl3.util.Position
import java.io.File

fun main() {
    GameTester()
}

class GameTester {
    private var showDemo = true
    private var showTest = false

    init {
        val engine: GameEngine = GameEngine.create(true)

//    val candara = FontType(engine.window, Texture.load("/home/matt/dev/kotlin/vecharia/assets/candara.png").id, File("/home/matt/dev/kotlin/vecharia/assets/candara.fnt"))
//    val text = GUIText("Text 2: Electric Boogaloo", 4f, candara, Position(y = 0.5f), 1.0f, true)
//
//    var color = 0
//    var red = 1f
//    var green = 0f
//    var blue = 0f


        while (engine.isRunning()) {
            engine.update()

//            engine.window.imGuiNewFrame() //todo create some wrapper around this which can also create a new frame
            engine.imGui().run {
//                newFrame()

                // Show demo
                if (showDemo)
                    showDemoWindow(::showDemo)

                run {
                    begin("Hello, World!", null, WindowFlag.AlwaysAutoResize.i)

                    text("This is some text")
                    if (!showTest)
                        checkbox("Demo Window", ::showDemo)
                    checkbox("Test Window", ::showTest)

                    colorEdit3("Background", engine.background)

                    text("Frame Speed: %.3f ms/frame (%.1f FPS)", Time.deltaTimeMs, 1000f / Time.deltaTimeMs)

                    end()

                    if (showTest) {
                        begin("Test Window", ::showTest)
                        text("This window is a test! Wow!")
                        if (button("Close Window"))
                            showTest = false
                        end()
                    }
                }
            }

            // Render

            engine.render()

            engine.tick()
        }
    }
}