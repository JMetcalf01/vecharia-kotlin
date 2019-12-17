package vecharia.render

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import vecharia.Input

/**
 * This class keeps track of the text on the screen.
 * It also allows you to print stuff to it.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param win the window instance
 * @param font the font instance
 */
class Canvas(private val win: Window, private val font: BitmapFont) {

    private var charBuffer: Array<Array<Character>> =
        Array(win.charHeight()) { Array(win.charWidth()) { Character(0.toChar(), Color.CLEAR) } }

    private var printing: Boolean = true

    // Position of the next character
    private var yi: Int = 0
    private var xi: Int = 0

    // Temporary variables when buffer and unbuffer is called
    private var charBufferTemp: Array<Array<Character>> =
        Array(win.charHeight()) { Array(win.charWidth()) { Character(0.toChar(), Color.CLEAR) } }
    private var yiTemp: Int = 0
    private var xiTemp: Int = 0

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
                    charBuffer[yi][xi + i] = Character(string[i], color)
                }
            }

            xi += string.length
        }
    }

    /**
     * This method just prints a new line.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun println() {
        if (printing) {
            if (yi < charBuffer.size - 3) {
                yi++
            } else {
                for (i in 1 until charBuffer.size) {
                    for (j in charBuffer[0].indices) {
                        charBuffer[i - 1][j] = Character(0.toChar(), Color.CLEAR)
                    }

                    for (j in charBuffer[0].indices) {
                        charBuffer[i - 1][j] = charBuffer[i][j]
                    }
                }

                for (i in charBuffer[0].indices) {
                    charBuffer[charBuffer.size - 1][i] = Character(0.toChar(), Color.CLEAR)
                }
            }

            xi = 0
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
                charBuffer[i][j] = Character(0.toChar(), Color.CLEAR)
            }
        }

        yi = 0
        xi = 0
    }

    /**
     * Renders the lines, and then user input, if any.
     *
     * @author Jonathan Metcalf
     * @since 1.2
     *
     * @param batch the batch of sprites
     */
    fun render(batch: SpriteBatch) {
        // Render previous lines
        for (i in charBuffer.indices) {
            for (j in charBuffer[0].indices) {
                font.color = if (charBuffer[i][j].color == Color.CLEAR) Color.WHITE else charBuffer[i][j].color
                font.draw(
                    batch, charBuffer[i][j].char.toString(),
                    j * font.spaceXadvance, win.height - font.lineHeight * i - 5
                )
            }
        }

        // Render user input
        if (printing) {
            font.color = Color.WHITE
            font.draw(
                batch, if (Input.typing) "> ${Input.current}" else Input.current, font.spaceXadvance * xi,
                win.height - font.lineHeight * yi - 5
            )
        }

        // Renders blinking cursor
        if (printing && Input.typing) {
            if (win.clock.frame / 20L % 2 == 0L) {
                font.draw(
                    batch, "_", font.spaceXadvance * (Input.length + xi + 2),
                    win.height - font.lineHeight * yi - 5
                )
            }
        }
    }

    /**
     * Buffers the character array in a temporary
     * character array for use with the pause menu.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun buffer() {
        charBufferTemp = charBuffer.copy()
        xiTemp = xi
        yiTemp = yi
        clear()
    }

    /**
     * Unbuffers the character array by copying
     * over the contents of the temporary character array.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun unbuffer() {
        if (charBufferTemp.isEmpty())
            throw IllegalStateException()

        xi = xiTemp
        yi = yiTemp
        charBuffer = charBufferTemp.copy()
        charBufferTemp = emptyArray()
    }

    /**
     * Deep copies a 2D character array.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    private fun Array<Array<Character>>.copy() = Array(size) { get(it).clone() }

    /**
     * A character that has a char and a color.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param char the character
     * @param color the color
     */
    class Character(val char: Char, val color: Color) {
        override fun toString(): String {
            return "[$char]"
        }
    }
}