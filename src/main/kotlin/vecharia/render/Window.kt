package vecharia.render

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

import com.badlogic.gdx.Input.Keys.*
import kotlin.math.roundToInt

/**
 * Handles the window of the game as well as user input.
 *
 * @author Matt Worzala
 * @since 1.0
 */
class Window : ApplicationAdapter() {
    lateinit var canvas: Canvas
    private lateinit var game: GameThread

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    var width: Int = -1
    var height: Int = -1

    private val inputActions = mutableMapOf<Int, InputEvent>()
    var entering = false
    var inputBuffer = ""
    var frameCount = 0

    /**
     * Create a new window and initialize fonts and the game thread.
     * Called by libGDX
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun create() {
        println("Beginning System Init...")

        width = Gdx.graphics.width
        height = Gdx.graphics.height
        println("Constants done")

        val f = FreeTypeFontGenerator(Gdx.files.absolute("assets/font.otf"))
        val fontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParams.size = 16
        font = f.generateFont(fontParams)
        font.setUseIntegerPositions(false)
        f.dispose()
        font.color = Color.WHITE
        println("Font done.")

        batch = SpriteBatch()
        canvas = Canvas(this, font)
        println("libGDX things done")

        SoundSystem.init(canvas)
        println("Sound done")

        game = GameThread(this)
        println("Main init done")
    }

    /**
     * Call the canvas renderer for text and wait for new key presses.
     * Called by libGDX
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun render() {
        frameCount++

        Gdx.gl.glClearColor(0F, 0F, 0F, 1F)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        canvas.render(batch)
        batch.end()

        if (entering) {
            readInput()

            if (Gdx.input.isKeyJustPressed(BACKSPACE) && inputBuffer.length > 0)
                inputBuffer = inputBuffer.substring(0, inputBuffer.length - 1)

            if (Gdx.input.isKeyJustPressed(ENTER))
                entering = false

            synchronized(inputActions) {
                for ((key, action) in inputActions) {
                    if (Gdx.input.isKeyJustPressed(key))
                        action.onInput()
                }
            }
        }
    }

    /**
     * Dispose of all assets loaded by the window.
     * Called by libGDX
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun dispose() {
        batch.dispose()
        font.dispose()
    }

    /**
     * Add a new key event listener to the window.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see com.badlogic.gdx.Input.Keys
     * @see vecharia.render.InputEvent
     * @param key the key to listen for, the int comes from the Keys class
     * @param event an InputAction
     */
    fun addKeyAction(key: Int, event: InputEvent) {
        synchronized(inputActions) {
            inputActions[key] = event
        }
    }

    /**
     * Add a key event listener from the window.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @see com.badlogic.gdx.Input.Keys
     * @param key the key to remove, the int comes from the Keys class
     */
    fun removeKeyAction(key: Int) {
        synchronized(inputActions) {
            inputActions.remove(key)
        }
    }

    /**
     * Read a single line of input from the user.
     * This is a thread blocking action, it will wait until the line has been submitted.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    fun readLine(): String {
        entering = true;
        game.getInput()
        val input = inputBuffer
        canvas.println(input, Color.WHITE)
        inputBuffer = ""
        return input
    }

    /**
     * The width of the screen, in characters, based on the current font.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the width of the screen in characters
     */
    fun charWidth(): Int {
        return (width.toDouble() / font.spaceXadvance).roundToInt()
    }

    /**
     * The height of the screen, in lines, based on the current font.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the height of the screen in characters
     */
    fun charHeight(): Int {
        return (height.toDouble() / font.lineHeight).roundToInt()
    }

    /**
     * Reads input for the current frame, and adds it to the buffer.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    private fun readInput() {
        val shift = Gdx.input.isKeyPressed(SHIFT_LEFT) || Gdx.input.isKeyPressed(SHIFT_RIGHT)
        for (i in A..Z) {
            if (Gdx.input.isKeyJustPressed(i))
                inputBuffer += (if (shift) i + 36 else i + 68).toChar()
        }

        for (i in NUM_0..NUM_1) {
            if (Gdx.input.isKeyJustPressed(i))
                inputBuffer += (i + 41).toChar()
        }

        if (Gdx.input.isKeyJustPressed(SPACE)) inputBuffer += ' ';
        if (Gdx.input.isKeyJustPressed(MINUS)) inputBuffer += '-';
        if (Gdx.input.isKeyJustPressed(PERIOD)) inputBuffer += '.';
    }
}