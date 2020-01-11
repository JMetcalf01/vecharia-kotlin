package vecharia.lwjgl3.opengl3.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13.*
import org.lwjgl.opengl.GL14.GL_TEXTURE_LOD_BIAS
import org.lwjgl.opengl.GL30.glGenerateMipmap
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack
import vecharia.lwjgl3.Disposable
import vecharia.lwjgl3.Garbage
import java.nio.ByteBuffer

class Texture private constructor(val id: Int, val width: Int, val height: Int) : Disposable {
    init { Garbage.dispose(this) }

    fun bind(unit: Int = 0) {
        glActiveTexture(GL_TEXTURE0 + unit)
        glBindTexture(GL_TEXTURE_2D, id)
    }

    override fun dispose() = GL11.glDeleteTextures(id)

    companion object {
        fun load(path: String): Texture {
            var width: Int = 0
            var height: Int = 0
            var buffer: ByteBuffer? = null

            MemoryStack.stackPush().use {
                val w = it.mallocInt(1)
                val h = it.mallocInt(1)
                val channels = it.mallocInt(1)

                buffer = STBImage.stbi_load(path, w, h, channels, 4)
                check(buffer != null) { "Failed to load image: \n${STBImage.stbi_failure_reason()}" }

                width = w.get()
                height = h.get()
            }

            val id = GL11.glGenTextures()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)

            // Generate Mip Map
            glGenerateMipmap(GL11.GL_TEXTURE_2D)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR)
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -1f) // todo this supposedly should be 0, but -1 looks better :/

            STBImage.stbi_image_free(buffer!!)

            return Texture(id, width, height)

        }
    }
}
