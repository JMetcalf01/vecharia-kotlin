package vecharia.lwjgl3

import vecharia.lwjgl3.font.TextMaster

class GameEngine private constructor(val window: Window): Tickable {
    private val tickables: MutableList<Tickable> = mutableListOf(window)

    fun update() {
        Time.update()
    }

    fun render() {
        TextMaster.render()
    }

    fun isRunning() = !window.shouldClose()

    fun register(tickable: Tickable) = tickables.add(tickable)

    override fun tick() = tickables.forEach { it.tick() }

    companion object {
        fun create(vsync: Boolean): GameEngine {
            val window = Window("Vecharia", 1920, 1080, vsync)
            Time.vsync = vsync
            Time.update()
            return GameEngine(window)
        }
    }
}