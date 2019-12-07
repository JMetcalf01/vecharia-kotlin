package vecharia.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/**
 * This class keeps track of the text on the screen.
 * It also allows you to print stuff to it.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 */
class Canvas(private val win: Window, private val font: BitmapFont) {
    private var charBuffer: Array<Array<Char>> = Array(win.charHeight()) { Array(win.charWidth()) { 0.toChar() } }
    private var fontColorBuffer: Array<Array<Color>> =
        Array(win.charHeight()) { Array(win.charWidth()) { Color.CLEAR } }
    private var printing: Boolean = true

    private var yi: Int = 0
    private var xi: Int = 0

    /**
     * This method prints a string by adding to the last string.
     * It then adds a new line.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param string the string to be printed
     * @param color the color to print in
     */
    fun println(string: String = "", color: Color = Color.CLEAR) {
        print(string, color)
        println()
    }

    /**
     * This method just prints a new line.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
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

    /**
     * This method prints a single character.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param char the character to be printed
     */
    fun print(char: Char) = print(char.toString())


    /**
     * This method prints a string str by adding to the last string.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param string the string to print
     * @param color the color to print in
     */
    fun print(string: String, color: Color = Color.WHITE) {
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


    /**
     * Clears the screen, buffers, x and y locations.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
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

    /**
     * Renders the lines, and then user input, if any.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param batch the batch of sprites
     */
    fun render(batch: SpriteBatch) {
        // Render previous lines
        for (i in charBuffer.indices) {
            for (j in charBuffer[0].indices) {
                font.color = if (fontColorBuffer[i][j] == Color.CLEAR) Color.WHITE else fontColorBuffer[i][j]
                font.draw(
                    batch, charBuffer[i][j].toString(),
                    j * font.spaceXadvance, win.height - font.lineHeight * i - 5
                )
            }
        }

        // Render user input
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