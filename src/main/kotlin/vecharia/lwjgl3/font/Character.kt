package vecharia.lwjgl3.font

class Character(
    val id: Int,
    val xTexture: Double,
    val yTexture: Double,
    xMaxTex: Double,
    yMaxTex: Double,
    val xOffset: Double,
    val yOffset: Double,
    val xSize: Double,
    val ySize: Double,
    val xAdvance: Double
) {
    val xMaxTexture = xTexture + xMaxTex
    val yMaxTexture = yTexture + yMaxTex
}