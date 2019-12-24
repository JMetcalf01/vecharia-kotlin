package vecharia.lwjgl3.font

import vecharia.lwjgl3.render.Mesh
import vecharia.lwjgl3.util.Color
import vecharia.lwjgl3.util.Colors
import vecharia.lwjgl3.util.Position

class GUIText(
    val text: String,
    val fontSize: Float,
    val font: FontType,
    val position: Position,
    val maxLineLength: Float,
    val centered: Boolean,
    var color: Color = Colors.WHITE
) {
    lateinit var mesh: Mesh
    var lineCount: Int = 0

    init {
        TextMaster.loadText(this)
    }

    fun remove() = TextMaster.removeText(this)
}