package vecharia.render

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

class Window : ApplicationAdapter() {
//    private lateinit var canvas: Canvas

    private lateinit var batch: SpriteBatch
    private lateinit var font: BitmapFont

    private var width: Int = -1
    private var height: Int = -1

    private var entering = false
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
//        canvas = Canvas()
        println("libGDX things done")

        SoundSystem.init()
        println("Sound done")

        println("Main init done")


    }


}