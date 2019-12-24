package vecharia.lwjgl3.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import vecharia.lwjgl3.font.FontType
import vecharia.lwjgl3.font.GUIText
import vecharia.lwjgl3.shader.GpuShader
import vecharia.lwjgl3.shader.UniformColor
import vecharia.lwjgl3.shader.UniformPosition
import vecharia.lwjgl3.shader.UniformSampler

class FontRenderer {
    private val shader: FontShader = FontShader()

    fun render(texts: Map<FontType, List<GUIText>>) {
        prepare()
        for (font in texts.keys) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.textureAtlas)
            for (text in texts[font] ?: listOf())
                render(text)
        }
        end()
    }

    private fun prepare() {
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        shader.start()
    }

    private fun render(text: GUIText) {
        text.mesh.bind()
        shader.color.load(text.color)
        shader.translation.load(text.position)
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.mesh.vertexCount)
        text.mesh.unbind()
    }

    private fun end() {
        shader.stop()
        GL11.glDisable(GL11.GL_BLEND)
    }
}

class FontShader : GpuShader("font") {
    // Vertex
    val translation: UniformPosition = UniformPosition("translation")

    // Fragment
    val color: UniformColor = UniformColor("color");
    private val fontAtlas: UniformSampler = UniformSampler("font_atlas")

    init {
        start()
        load(translation, color, fontAtlas)
        fontAtlas.load(0)
        stop()
    }
}