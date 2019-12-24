package vecharia.lwjgl3.util

data class Color(var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f, var a: Float = 0.0f)

object Colors {
    val BLACK = Color()
    val WHITE = Color(1.0f, 1.0f, 1.0f)
    val RED = Color(r = 1.0f)
    val GREEN = Color(g = 1.0f)
    val BLUE = Color(b = 1.0f)
}