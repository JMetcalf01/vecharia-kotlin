package vecharia.lwjgl3

import vecharia.lwjgl3.util.Color

//todo eventually make glow a class if we want to configure anything besides color
class Text(val raw: String = "", val border: Color? = null, val glow: Color? = null) {
    private val children: MutableList<Text> = ArrayList()

    fun append(text: Text) {
        children.add(text)
    }

    operator fun plusAssign(text: Text) = append(text)

    operator fun plusAssign(text: String) = append(Text(text))
}