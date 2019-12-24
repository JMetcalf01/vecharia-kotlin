package vecharia.lwjgl3.render

import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryStack
import vecharia.lwjgl3.Disposable
import java.nio.FloatBuffer

class Mesh(positions: FloatArray, textureCoords: FloatArray): Disposable {
    private val vbos: MutableMap<Int, Int> = mutableMapOf()

    private val id: Int = GL30.glGenVertexArrays()
    val vertexCount: Int = positions.size

    init {
        GL30.glBindVertexArray(id)
        vbos[0] = putDataInVbo(0, positions, 2)
        vbos[1] = putDataInVbo(1, textureCoords, 2)
        GL30.glBindVertexArray(0)
    }

    fun bind() {
        GL30.glBindVertexArray(id)
        vbos.keys.forEach { GL30.glEnableVertexAttribArray(it) }
    }

    fun unbind() {
        vbos.keys.forEach { GL30.glDisableVertexAttribArray(it) }
        GL30.glBindVertexArray(0)
    }

    override fun dispose() {
        unbind()

        GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0)
        vbos.values.forEach { GL15.glDeleteBuffers(it) }

        GL30.glDeleteVertexArrays(id)
    }

    private fun putDataInVbo(list: Int, data: FloatArray, dimensions: Int): Int {
        val vbo = GL15.glGenBuffers()
        MemoryStack.stackPush().use {
            val buffer: FloatBuffer = it.mallocFloat(data.size)
            buffer.put(data).flip()
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW)
            GL30.glVertexAttribPointer(list, dimensions, GL30.GL_FLOAT, false, 0, 0)
        }
        return vbo
    }
}