package vecharia.lwjgl3.font

class TextMeshData(val positions: FloatArray, val textureCoords: FloatArray) {
    val vertexCount: Int
        get() = positions.size / 2
}