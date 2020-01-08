package vecharia.lwjgl3

import imgui.WindowFlag
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBTTAlignedQuad
import org.lwjgl.stb.STBTruetype.*
import org.lwjgl.system.MemoryUtil
import uno.buffer.memFree
import vecharia.filesystem.GameFiles
import vecharia.lwjgl3.text.Font
import vecharia.lwjgl3.text.FontSize
import vecharia.lwjgl3.text.Fonts
import java.nio.FloatBuffer
import kotlin.math.ceil
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.pow

fun main() {
    GameTester()
}

class GameTester {
    private val q: STBTTAlignedQuad = STBTTAlignedQuad.malloc()
    private val xb: FloatBuffer = MemoryUtil.memAllocFloat(1)
    private val yb: FloatBuffer = MemoryUtil.memAllocFloat(1)

    private val supportsGammaCorrection: Boolean
    private var gammaCorrection = false
    private var translation = 0f
    private var size = intArrayOf(0)
    private val color: FloatArray = floatArrayOf(1f, 0f, 0f)

    private var showDemo = false
    private var showTest = false

    private val font: Font

    init {
        val engine: GameEngine = GameEngine.create(true)

        Fonts.init()
        font = Fonts.OLD_DEFAULT

        val caps = GL.getCapabilities()
        supportsGammaCorrection = caps.OpenGL30 || caps.GL_ARB_framebuffer_sRGB || caps.GL_EXT_framebuffer_sRGB

//        initFont()

        while (engine.isRunning()) {
            engine.update()

            //PP.IF production
            engine.imGui {
                // Show demo
                if (showDemo)
                    showDemoWindow(::showDemo)

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
            engine.imGui {
                begin("Font Stuff", null, WindowFlag.AlwaysAutoResize.i)

                checkbox("Gamma Correction", ::gammaCorrection)
//                sliderFloat("Font Size", ::size, 12f, 48f)
                sliderFloat("Translation", ::translation, 0f, 500f)
                colorEdit3("Text Color", color)

                combo("Font Size", size, this@GameTester.font.sizes.map { it.name.toLowerCase().capitalize() }, 10)

                end()
            }
            //PP.ENDIF

            // Render
            draw(engine)

            engine.tick()
        }

        memFree(yb, xb)
        q.free()
    }

    private fun draw(engine: GameEngine) {
        // Draw Init Stuff
        glDisable(GL_CULL_FACE)
        glDisable(GL_TEXTURE_2D)
        glDisable(GL_LIGHTING)
        glDisable(GL_DEPTH_TEST)

        glMatrixMode(GL_PROJECTION)
        glLoadIdentity()
        glOrtho(0.0, engine.window.width + 0.0, engine.window.height + 0.0, 0.0, -1.0, 1.0)
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()

        var x = 20f

        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        if (gammaCorrection)
            glEnable(GL30.GL_FRAMEBUFFER_SRGB)

        glColor3f(color[0], color[1], color[2])

        // todo the font argument in print is the size to use according to the sizes array.
        print(80f, 80f, FontSize.DEFAULT, "Truetype font is very cool because it is based")
        print(80f, 104f, FontSize.DEFAULT, "on vectors, meaning there is no image supplied when loading it.")
        print(80f, 128f, FontSize.DEFAULT, "the benefit is that we can generate an image (texture atlas) for")
        print(80f, 154f, FontSize.DEFAULT, "any size font we desire without worrying about pixelation due to scaling.")

        print(80f, 200f, FontSize.DEFAULT, "This can be seen by testing out some of the larger font sizes")
        print(80f, 224f, FontSize.DEFAULT, "and noting that the symbols stay very crisp throughout scaling.")
        print(80f, 248f, FontSize.DEFAULT, "(the missing letters are expected, don't worry)")

        glMatrixMode(GL_MODELVIEW)
        glTranslatef(200f, 350f, 0f)

        x += translation

        val fontSize = FontSize.values()[size[0]]
        val lineHeight = fontSize.size

        print(x, 100f + lineHeight * 0, fontSize, "This is a test")
        print(x, 100f + lineHeight * 1, fontSize, "Now is the time for all good men to come to the aid of their country.")
        print(x, 100f + lineHeight * 2, fontSize, "The quick brown fox jumps over the lazy dog.")
        print(x, 100f + lineHeight * 3, fontSize, "0123456789")
        print(x, 100f + lineHeight * 4, fontSize, "~!@#$%^&*(){}[];':,.<>/?")

        glDisable(GL30.GL_FRAMEBUFFER_SRGB)
    }

    private fun drawBoxTC(x0: Float, y0: Float, x1: Float, y1: Float, s0: Float, t0: Float, s1: Float, t1: Float) {
        glTexCoord2f(s0, t0)
        glVertex2f(x0, y0)
        glTexCoord2f(s1, t0)
        glVertex2f(x1, y0)
        glTexCoord2f(s1, t1)
        glVertex2f(x1, y1)
        glTexCoord2f(s0, t1)
        glVertex2f(x0, y1)
    }

    private fun print(x: Float, y: Float, size: FontSize, text: String) {
        xb.put(0, x)
        yb.put(0, y)

        font.data.position(font.sizes.indexOf(size) * 128)

        glEnable(GL_TEXTURE_2D)
        glBindTexture(GL_TEXTURE_2D, font.texture)

        glBegin(GL_QUADS)
        for (i in text.indices) {
            stbtt_GetPackedQuad(font.data, BITMAP_W, BITMAP_H, text[i].toInt(), xb, yb, q, false)
            drawBoxTC(q.x0(), q.y0(), q.x1(), q.y1(), q.s0(), q.t0(), q.s1(), q.t1())
        }
        glEnd()
    }

    companion object {
        private const val BITMAP_W = 4096 //todo VVV something like 1024 is probably fine? There might be some calculation for this, I should try to find that out or come up with an estimation.
        private const val BITMAP_H = 4096 //todo this will limit font size to some degree, but higher resolution is more costly

        private val SCALE = listOf(12f, 24f, 48f, 192f)

        private val FONT = GameFiles.get("assets/Ubuntu-M.ttf")
    }
}