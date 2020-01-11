package vecharia.lwjgl3.opengl3.shader

import org.lwjgl.opengl.GL20
import vecharia.lwjgl3.util.Color
import vecharia.lwjgl3.util.Position

abstract class UniformVariable<T>(private val name: String) {
    protected var location: Int = -1
        get() {
            check(field != -1) { "Cannot use unloaded location of uniform variable!" }
            return field
        }

    fun init(program: Int) {
        location = GL20.glGetUniformLocation(program, name)
        if (location == -1)
            System.err.println("Failed to locate uniform variable with name '$name'.")
    }

    abstract fun load(t: T)
}

class UniformBoolean(name: String) : UniformVariable<Boolean>(name) {
    override fun load(t: Boolean) = GL20.glUniform1i(location, if (t) 1 else 0)
}

class UniformInt(name: String) : UniformVariable<Int>(name) {
    override fun load(t: Int) = GL20.glUniform1i(location, t)
}

class UniformFloat(name: String) : UniformVariable<Float>(name) {
    override fun load(t: Float) = GL20.glUniform1f(location, t)
}

class UniformSampler(name: String) : UniformVariable<Int>(name) {
    override fun load(t: Int) = GL20.glUniform1i(location, t)
}

class UniformColor(name: String) : UniformVariable<Color>(name) {
    override fun load(t: Color) = GL20.glUniform4f(location, t.r, t.g, t.b, t.a)
}

class UniformPosition(name: String) : UniformVariable<Position>(name) {
    override fun load(t: Position) = GL20.glUniform2f(location, t.x, t.y)
}

