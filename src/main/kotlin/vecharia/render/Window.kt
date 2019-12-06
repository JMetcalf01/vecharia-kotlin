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

class Window : ApplicationAdapter() {
    private lateinit var canvas: Canvas
    private lateinit var game: GameThread

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    private var width: Int = -1
    private var height: Int = -1

    private val inputActions = mutableMapOf<Int, InputAction>()
    var entering = false
    private var inputBuffer = ""
    private var frameCount = 0

    override fun create() {
        println("Beginning System Init...")

        width = Gdx.graphics.width
        height = Gdx.graphics.height
        println("Constants done")

        val f = FreeTypeFontGenerator(Gdx.files.absolute("assets/font.otf"))
        val ftfp = FreeTypeFontGenerator.FreeTypeFontParameter()
        ftfp.size = 16
        font = f.generateFont(ftfp)
        font.setUseIntegerPositions(false)
        f.dispose()
        font.color = Color.WHITE
        println("Font done.")

        batch = SpriteBatch()
        canvas = Canvas(this, font)
        println("libGDX things done")

        SoundSystem.init()
        println("Sound done")

        game = GameThread(this)
        println("Main init done")
    }

    override fun render() {
        frameCount++

        Gdx.gl.glClearColor(0F, 0F, 0F, 1F)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        canvas.render(batch)
        batch.end()

        if (entering) {
            addToInputBuffer()

            if (Gdx.input.isKeyJustPressed(BACKSPACE) && inputBuffer.length > 0)
                inputBuffer = inputBuffer.substring(0, inputBuffer.length - 1)

            if (Gdx.input.isKeyJustPressed(ENTER))
                entering = false

            synchronized(inputActions) {
                for ((key, action) in inputActions) {
                    if (Gdx.input.isKeyJustPressed(key))
                        action.action()
                }
            }
        }
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }

    fun addKeyAction(key: Int, action: InputAction) {
        synchronized(inputActions) {
            inputActions[key] = action
        }
    }

    fun removeKeyAction(key: Int) {
        synchronized(inputActions) {
            inputActions.remove(key)
        }
    }

    fun readLine(): String {
        entering = true;
        game.pause()
        val input = inputBuffer
//        canvas.println(input, Color.WHITE)
        inputBuffer = ""
        return input
    }

    fun charWidth(): Int {
        return (width.toDouble() / font.spaceXadvance).roundToInt()
    }

    fun charHeight(): Int {
        return (height.toDouble() / font.lineHeight).roundToInt()
    }

    private fun addToInputBuffer() {
        if (Gdx.input.isKeyPressed(SHIFT_LEFT) || Gdx.input.isKeyPressed(SHIFT_RIGHT)) {
            if (Gdx.input.isKeyJustPressed(A)) inputBuffer += 'A';
            if (Gdx.input.isKeyJustPressed(B)) inputBuffer += 'B';
            if (Gdx.input.isKeyJustPressed(C)) inputBuffer += 'C';
            if (Gdx.input.isKeyJustPressed(D)) inputBuffer += 'D';
            if (Gdx.input.isKeyJustPressed(E)) inputBuffer += 'E';
            if (Gdx.input.isKeyJustPressed(F)) inputBuffer += 'F';
            if (Gdx.input.isKeyJustPressed(G)) inputBuffer += 'G';
            if (Gdx.input.isKeyJustPressed(H)) inputBuffer += 'H';
            if (Gdx.input.isKeyJustPressed(I)) inputBuffer += 'I';
            if (Gdx.input.isKeyJustPressed(J)) inputBuffer += 'J';
            if (Gdx.input.isKeyJustPressed(K)) inputBuffer += 'K';
            if (Gdx.input.isKeyJustPressed(L)) inputBuffer += 'L';
            if (Gdx.input.isKeyJustPressed(M)) inputBuffer += 'M';
            if (Gdx.input.isKeyJustPressed(N)) inputBuffer += 'N';
            if (Gdx.input.isKeyJustPressed(O)) inputBuffer += 'O';
            if (Gdx.input.isKeyJustPressed(P)) inputBuffer += 'P';
            if (Gdx.input.isKeyJustPressed(Q)) inputBuffer += 'Q';
            if (Gdx.input.isKeyJustPressed(R)) inputBuffer += 'R';
            if (Gdx.input.isKeyJustPressed(S)) inputBuffer += 'S';
            if (Gdx.input.isKeyJustPressed(T)) inputBuffer += 'T';
            if (Gdx.input.isKeyJustPressed(U)) inputBuffer += 'U';
            if (Gdx.input.isKeyJustPressed(V)) inputBuffer += 'V';
            if (Gdx.input.isKeyJustPressed(W)) inputBuffer += 'W';
            if (Gdx.input.isKeyJustPressed(X)) inputBuffer += 'X';
            if (Gdx.input.isKeyJustPressed(Y)) inputBuffer += 'Y';
            if (Gdx.input.isKeyJustPressed(Z)) inputBuffer += 'Z';
        } else {
            if (Gdx.input.isKeyJustPressed(A)) inputBuffer += 'a';
            if (Gdx.input.isKeyJustPressed(B)) inputBuffer += 'b';
            if (Gdx.input.isKeyJustPressed(C)) inputBuffer += 'c';
            if (Gdx.input.isKeyJustPressed(D)) inputBuffer += 'd';
            if (Gdx.input.isKeyJustPressed(E)) inputBuffer += 'e';
            if (Gdx.input.isKeyJustPressed(F)) inputBuffer += 'f';
            if (Gdx.input.isKeyJustPressed(G)) inputBuffer += 'g';
            if (Gdx.input.isKeyJustPressed(H)) inputBuffer += 'h';
            if (Gdx.input.isKeyJustPressed(I)) inputBuffer += 'i';
            if (Gdx.input.isKeyJustPressed(J)) inputBuffer += 'j';
            if (Gdx.input.isKeyJustPressed(K)) inputBuffer += 'k';
            if (Gdx.input.isKeyJustPressed(L)) inputBuffer += 'l';
            if (Gdx.input.isKeyJustPressed(M)) inputBuffer += 'm';
            if (Gdx.input.isKeyJustPressed(N)) inputBuffer += 'n';
            if (Gdx.input.isKeyJustPressed(O)) inputBuffer += 'o';
            if (Gdx.input.isKeyJustPressed(P)) inputBuffer += 'p';
            if (Gdx.input.isKeyJustPressed(Q)) inputBuffer += 'q';
            if (Gdx.input.isKeyJustPressed(R)) inputBuffer += 'r';
            if (Gdx.input.isKeyJustPressed(S)) inputBuffer += 's';
            if (Gdx.input.isKeyJustPressed(T)) inputBuffer += 't';
            if (Gdx.input.isKeyJustPressed(U)) inputBuffer += 'u';
            if (Gdx.input.isKeyJustPressed(V)) inputBuffer += 'v';
            if (Gdx.input.isKeyJustPressed(W)) inputBuffer += 'w';
            if (Gdx.input.isKeyJustPressed(X)) inputBuffer += 'x';
            if (Gdx.input.isKeyJustPressed(Y)) inputBuffer += 'y';
            if (Gdx.input.isKeyJustPressed(Z)) inputBuffer += 'z';
        }

        if (Gdx.input.isKeyJustPressed(NUM_0)) inputBuffer += '0';
        if (Gdx.input.isKeyJustPressed(NUM_1)) inputBuffer += '1';
        if (Gdx.input.isKeyJustPressed(NUM_2)) inputBuffer += '2';
        if (Gdx.input.isKeyJustPressed(NUM_3)) inputBuffer += '3';
        if (Gdx.input.isKeyJustPressed(NUM_4)) inputBuffer += '4';
        if (Gdx.input.isKeyJustPressed(NUM_5)) inputBuffer += '5';
        if (Gdx.input.isKeyJustPressed(NUM_6)) inputBuffer += '6';
        if (Gdx.input.isKeyJustPressed(NUM_7)) inputBuffer += '7';
        if (Gdx.input.isKeyJustPressed(NUM_8)) inputBuffer += '8';
        if (Gdx.input.isKeyJustPressed(NUM_9)) inputBuffer += '9';
        if (Gdx.input.isKeyJustPressed(SPACE)) inputBuffer += ' ';
        if (Gdx.input.isKeyJustPressed(MINUS)) inputBuffer += '-';
        if (Gdx.input.isKeyJustPressed(PERIOD)) inputBuffer += '.';
    }


}