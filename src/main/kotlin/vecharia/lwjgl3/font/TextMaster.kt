package vecharia.lwjgl3.font

import vecharia.lwjgl3.render.FontRenderer
import vecharia.lwjgl3.render.Mesh

object TextMaster {
    private val texts: MutableMap<FontType, MutableList<GUIText>> = mutableMapOf()
    private val renderer: FontRenderer = FontRenderer()

    fun render() = renderer.render(texts)

    fun loadText(text: GUIText) {
        val data = text.font.loadText(text)
        text.mesh = Mesh(data.positions, data.textureCoords)

        val batch = texts.computeIfAbsent(text.font) { mutableListOf() }
        batch.add(text)
    }

    fun removeText(text: GUIText) {
        val textBatch: MutableList<GUIText> = texts[text.font] ?: mutableListOf()
        textBatch.remove(text)
        if (textBatch.isEmpty())
            texts.remove(text.font)
    }
}