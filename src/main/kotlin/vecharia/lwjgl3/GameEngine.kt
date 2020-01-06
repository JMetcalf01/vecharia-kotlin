package vecharia.lwjgl3

import glm_.vec4.Vec4
import imgui.ImGui
import org.lwjgl.opengl.GL11

class GameEngine private constructor(val window: Window): Tickable {
    private val tickables: MutableList<Tickable> = mutableListOf(window)

    val background = Vec4(0.0f, 0.0f, 0.0f, 1f);

    fun update() {
        Time.update()
    }

    fun render() {
        ImGui.render()
        GL11.glClearColor(background.r, background.g, background.b, background.a)
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        window.imGuiRender()
    }

    fun isRunning() = !window.shouldClose()

    fun register(tickable: Tickable) = tickables.add(tickable)

    override fun tick() = tickables.forEach { it.tick() }

    fun imGui(): ImGui {
        window.imGuiNewFrame()
        ImGui.newFrame()
        return ImGui
    }

    companion object {
        fun create(vsync: Boolean): GameEngine {
            val window = Window("Vecharia", 1920, 1080, vsync)
            Time.vsync = vsync
            Time.update()
            return GameEngine(window)
        }
    }
}