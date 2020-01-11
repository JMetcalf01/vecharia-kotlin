package vecharia.lwjgl3.opengl3.shader

import org.lwjgl.opengl.GL20
import vecharia.filesystem.GameFile
import vecharia.filesystem.GameFiles
import vecharia.lwjgl3.Disposable
import vecharia.lwjgl3.Garbage

open class GpuShader(vertex: GameFile, fragment: GameFile) : Disposable {
    private val id: Int = GL20.glCreateProgram()

    constructor(name: String) : this(GameFiles.get("src/main/kotlin/vecharia/lwjgl3/shader/program/v_${name}.glsl"), GameFiles.get("src/main/kotlin/vecharia/lwjgl3/shader/program/f_${name}.glsl"))

    init {
        check(id != 0) { "Failed to create shader program!" }

        val vId: Int = loadShader(vertex.toString(), vertex.text(), GL20.GL_VERTEX_SHADER)
        val fId: Int = loadShader(fragment.toString(), fragment.text(), GL20.GL_FRAGMENT_SHADER)

        GL20.glLinkProgram(id)
        check(GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) != 0) { "Failed to link shader!" + GL20.glGetProgramInfoLog(id, 1024) }

        if (vId != 0) GL20.glDetachShader(id, vId)
        if (fId != 0) GL20.glDetachShader(id, fId)

        GL20.glValidateProgram(id)
        check(GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) != 0) { "Failed to validate shader!" + GL20.glGetProgramInfoLog(id, 1024) } //todo this should only be validated when developing, because we know they compile otherwise

        Garbage.dispose(this)
    }

    protected fun load(vararg uniforms: UniformVariable<*>) = uniforms.forEach { it.init(id) }

    fun start() = GL20.glUseProgram(id)

    fun stop() = GL20.glUseProgram(0)

    override fun dispose() { stop(); GL20.glDeleteProgram(id) }

    private fun loadShader(name: String, source: String, type: Int): Int {
        val sId = GL20.glCreateShader(type)
        check(sId != 0) { "Failed to load $name!" }

        GL20.glShaderSource(sId, source)
        GL20.glCompileShader(sId)
        check(GL20.glGetShaderi(sId, GL20.GL_COMPILE_STATUS) != 0) { "Failed to compile " + name + "!" + GL20.glGetShaderInfoLog(sId, 1024) }

        GL20.glAttachShader(this.id, sId)
        return sId
    }
}