package vecharia.lwjgl3.font

import vecharia.lwjgl3.Window
import java.io.File

class FontType(window: Window, val textureAtlas: Int, fontFile: File) {
    private val loader: TextMeshCreator = TextMeshCreator(window, fontFile)

    fun loadText(text: GUIText): TextMeshData = loader.createTextMesh(text)
}