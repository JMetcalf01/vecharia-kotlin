package vecharia.lwjgl3.font

class Word(val fontSize: Double) {
    val characters: MutableList<Character> = mutableListOf()
    var width: Double = 0.0

    fun add(character: Character) {
        characters.add(character)
        width += character.xAdvance * fontSize
    }
}