package vecharia.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Canvas(private val win: Window, private val font: BitmapFont) {

    private var charBuffer: Array<Array<Char>> = Array(win.charHeight()) { Array(win.charWidth()) { 0.toChar() } }
    private var fontColorBuffer: Array<Array<Color>> = Array(win.charHeight()) { Array(win.charWidth()) { Color.CLEAR } }
    private var printing: Boolean = true

    private var yi: Int = 0
    private var xi: Int = 0

    fun println(string: String, color: Color) {
        print(string, color)
        println()
    }

    fun println(string: String = "") {
        println(string, Color.WHITE)
    }

    private fun println() {
        if (printing) {
            if (yi < charBuffer.size - 3) {
                yi++
            } else {
                for (i in 1 until charBuffer.size) {
                    for (j in charBuffer[0].indices) {
                        charBuffer[i - 1][j] = 0.toChar()
                        fontColorBuffer[i - 1][j] = Color.CLEAR
                    }

                    for (j in charBuffer[0].indices) {
                        charBuffer[i - 1][j] = charBuffer[i][j]
                        fontColorBuffer[i - 1][j] = fontColorBuffer[i][j]
                    }
                }

                for (i in charBuffer[0].indices) {
                    charBuffer[charBuffer.size - 1][i] = 0.toChar()
                    fontColorBuffer[charBuffer.size - 1][i] = Color.CLEAR
                }
            }

            xi = 0
        }
    }

    fun print(string: String, color: Color) {
        if (printing) {
            for (i in string.indices) {
                if (xi + i < charBuffer[0].size) {
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
        for (i in charBuffer.indices) {
            for (j in charBuffer[0].indices) {
                charBuffer[i][j] = 0.toChar()
                fontColorBuffer[i][j] = Color.CLEAR
            }
        }

        yi = 0
        xi = 0
    }


    fun render(batch: SpriteBatch) {
        for (i in charBuffer.indices) {
            for (j in charBuffer[0].indices) {
                font.color = if (fontColorBuffer[i][j] == Color.CLEAR) Color.WHITE else fontColorBuffer[i][j]
                font.draw(batch, charBuffer[i][j].toString(),
                    j * font.spaceXadvance, win.height - font.lineHeight * i - 5)
            }
        }

        if (printing) {
            font.color = Color.WHITE
            font.draw(
                batch, win.inputBuffer, font.spaceXadvance * xi,
                win.height - font.lineHeight * yi - 5
            )
        }

        if (printing && win.entering) {
            if (win.frameCount / 20 % 2 == 0) {
                font.draw(
                    batch, "_", font.spaceXadvance * (win.inputBuffer.length + xi),
                    win.height - font.lineHeight * yi - 5
                )
            }
        }
    }
}