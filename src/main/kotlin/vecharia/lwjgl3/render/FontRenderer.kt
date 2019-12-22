package vecharia.lwjgl3.render

import vecharia.lwjgl3.shader.GpuShader
import vecharia.lwjgl3.shader.UniformColor
import vecharia.lwjgl3.shader.UniformPosition
import vecharia.lwjgl3.shader.UniformSampler

class FontRenderer {
    private val shader: FontShader = FontShader()
}

class FontShader : GpuShader("font") {
    // Vertex
    val translation: UniformPosition = UniformPosition("translation")

    // Fragment
    val color: UniformColor = UniformColor("color");
    val fontAtlas: UniformSampler = UniformSampler("font_atlas")

    init {
        start()
        load(translation, color, fontAtlas)
        fontAtlas.load(0)
        stop()
    }
}