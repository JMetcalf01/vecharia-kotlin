package vecharia.render

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import vecharia.Input
import vecharia.Vecharia
import vecharia.logging.ConsoleLogger
import vecharia.logging.Logger
import vecharia.menu.PauseMenu
import vecharia.settings.Settings
import vecharia.util.GameState
import java.awt.Toolkit
import kotlin.math.roundToInt
import kotlin.system.exitProcess

/**
 * The entry point into the program.
 *
 * @author Matt Worzala and Jonathan Metcalf
 * @since 1.0
 */
fun main() {
    Settings.load()

    val config = LwjglApplicationConfiguration()
    config.height = Toolkit.getDefaultToolkit().screenSize.height
    config.width = Toolkit.getDefaultToolkit().screenSize.width
    config.fullscreen = Settings.fullscreen
    config.resizable = false

    LwjglApplication(Window(), config)
}

/**
 * Handles the window of the game as well as user input.
 *
 * @author Matt Worzala
 * @since 1.0
 */
class Window : ApplicationAdapter() {
    lateinit var canvas: Canvas
    lateinit var game: Vecharia
    lateinit var clock: Clock
    private lateinit var pause: PauseMenu

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    private var width: Int = -1
    var height: Int = -1

    var exit: Boolean = false

    /**
     * Create a new window and initialize fonts and the game thread.
     * Called by libGDX
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun create() {
        val logger: Logger = ConsoleLogger(Logger.Level.DEBUG)
        logger.info("Initializing System")

        width = Gdx.graphics.width
        height = Gdx.graphics.height
        logger.info("Constants done")

        val f = FreeTypeFontGenerator(Gdx.files.absolute("assets/consola.ttf"))
        val fontParams = FreeTypeFontGenerator.FreeTypeFontParameter()
        fontParams.size = 16
        font = f.generateFont(fontParams)
        font.setUseIntegerPositions(false)
        f.dispose()
        font.color = Color.WHITE
        logger.info("Font done")

        batch = SpriteBatch()
        canvas = Canvas(this, font)
        logger.info("libGDX initialized")

        game = Vecharia(ConsoleLogger(Logger.Level.DEBUG), this)
        game.start()
        pause = PauseMenu(game)

        clock = Clock(game)
        clock[GameState.UNLOADED] = game
        clock[GameState.ACTIVE] = game
        clock[GameState.PAUSED] = pause
        logger.info("Clock thread done")

        SoundSystem.init(canvas)
        if(Settings.musicEnabled) {
            SoundSystem.add("assets/introscreen.mp3", true)
            SoundSystem.playM()
        }
        logger.info("Sound done")

        logger.info("Initialization finished")
    }

    /**
     * Call the canvas renderer for text and wait for new key presses.
     * Called by libGDX
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun render() {
        if (exit) {
            game.log.info("Exiting")
            game.window.dispose()
            Gdx.app.exit()
            SoundSystem.end()
            exitProcess(0)
        }

        Gdx.gl.glClearColor(0F, 0F, 0F, 1F)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        canvas.render(batch)
        batch.end()

        Input.readGdxInput()
    }

    /**
     * Dispose of all assets loaded by the window.
     * Called by libGDX.
     *
     * @author Matt Worzala
     * @since 1.0
     */
    override fun dispose() {
        batch.dispose()
        font.dispose()
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
}