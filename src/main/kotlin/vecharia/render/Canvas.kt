package vecharia.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Canvas(private val win: Window, private val font: BitmapFont) {

    private var charBuffer
    private var fontColorBuffer
    private var printing: Boolean = true

    private var yi: Int = 0
    private var xi: Int = 0

    fun println(string: String, color: Color) {
        print(string, color)
        println()
    }

    fun println(string: String) {
        println(string, Color.WHITE)
    }

    fun println() {
        if (printing) {
            if (yi < charBuffer.length - 3) {
                yi++
            } else {
                for (i in 1 until charBuffer.length) {
                    for (j in 0..charBuffer[0].length) {
                        charBuffer[i - 1][j] = 0
                        fontColorBuffer[i - 1][j] = null
                    }

                    for (j in 0..charBuffer[0].length) {
                        charBuffer[i - 1][j] = charBuffer[i][j]
                        fontColorBuffer[i - 1][j] = fontColorBuffer[i][j]
                    }
                }

                for (i in 0..charBuffer[0].length) {
                    charBuffer[charBuffer.length - 1][i] = 0
                    fontColorBuffer[charBuffer.length - 1][i] = null
                }
            }

            xi = 0
        }
    }

    fun print(string: String, color: Color) {
        if (printing) {
            for (i in 0..string.length) {
                if (xi + i < charBuffer[0].length) {
                    charBuffer[yi][xi + i] = string[i]
                    fontColorBuffer[yi][xi + i] = color
                }
            }

            xi = string.length
        }

    }

    fun print(string: String) {
        print(string, Color.WHITE)
    }


    fun clear() {
        for (i in 0..charBuffer.length) {
            for (j in 0..charBuffer[0].length) {
                charBuffer[i][j] = 0
                fontColorBuffer[i][j] = null
            }
        }

        yi = 0
        xi = 0
    }


    fun render(batch: SpriteBatch) {
        for (i in 0..charBuffer.length) {
            for (j in charBuffer[0].length) {
                font.setColor(fontColorBuffer == null ? Color . WHITE : fontColorBuffer [i][j])
                font.draw(batch)
            }
        }

        if (printing) {
            font.color = Color.WHITE
            font.draw(
                batch, Window.inputBuffer, font.spaceXadvance * xi,
                Window.WIN_HEIGHT - font.lineHeight * yi - 5
            )
        }

        if (printing && Window.entering) {
            if (Window.framecount / 20 % 2 == 0) {
                font.draw(
                    batch, "_", font.spaceXadvance * (Window.inputBuffer.length + xi),
                    Window.WIN_HEIGHT - font.lineHeight * yi - 5
                )
            }
        }
    }
}