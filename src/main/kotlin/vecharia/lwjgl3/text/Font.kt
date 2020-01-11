package vecharia.lwjgl3.text

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBTruetype.*
import org.lwjgl.stb.STBTTPackContext
import org.lwjgl.stb.STBTTPackedchar
import org.lwjgl.system.MemoryUtil.NULL
import vecharia.filesystem.GameFiles
import vecharia.lwjgl3.Disposable
import vecharia.lwjgl3.Garbage
import java.lang.RuntimeException

class Font private constructor(val texture: Int, val data: STBTTPackedchar.Buffer, val sizes: List<FontSize>) : Disposable {

    init {
        Garbage.dispose(this)
    }

    override fun dispose() {
        glDeleteTextures(texture)
        data.free()
    }

    companion object {
        fun load(name: String, vararg sizes: FontSize): Font {
            val fontFile = GameFiles.get("assets/$name.ttf")
            if (!fontFile.exists())
                throw RuntimeException("Failed to locate font file at '${fontFile.absolute()}'!") //todo proper error handling

            val texture = glGenTextures()
            val data = STBTTPackedchar.malloc(6 * 128)

            STBTTPackContext.malloc().use {
                val fontBuffer = fontFile.buffer() ?: throw RuntimeException("Failed to buffer font file at '${fontFile.absolute()}'!") //todo proper error handling
                val bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H)

                // Pack each font size into the texture atlas
                stbtt_PackBegin(it, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL)
                for (i in sizes.indices) {
                    val p = i * 128 + ASCII_START
                    data.limit(p + ASCII_LIMIT)
                    data.position(p)
                    stbtt_PackSetOversampling(it, 2, 2)
                    stbtt_PackFontRange(it, fontBuffer, 0, sizes[i].size, ASCII_START, data)
                }
                data.clear()
                stbtt_PackEnd(it)

                // Bind atlas to texture
                glBindTexture(GL_TEXTURE_2D, texture)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            }
            return Font(texture, data, sizes.toList())
        }

        private const val BITMAP_W = 4096
        private const val BITMAP_H = 4096

        private const val ASCII_START = 32
        private const val ASCII_LIMIT = 95
    }
}

object Fonts {
    lateinit var UBUNTU_MONO: Font
    lateinit var OLD_DEFAULT: Font

    fun init() {
        UBUNTU_MONO = Font.load("Ubuntu-M", FontSize.DEFAULT, FontSize.TITLE, FontSize.SUBTITLE, FontSize.EXTRALARGEFUCKOFFTEST)
        OLD_DEFAULT = Font.load("font", FontSize.DEFAULT, FontSize.TITLE, FontSize.SUBTITLE, FontSize.EXTRALARGEFUCKOFFTEST)
    }
}

enum class FontSize(val size: Float) {
    DEFAULT(24f), TITLE(48f), SUBTITLE(12f), EXTRALARGEFUCKOFFTEST(192f);
}