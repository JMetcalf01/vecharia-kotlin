package vecharia.lwjgl3.font

class Line(val maxLength: Double, spaceWidth: Double, fontSize: Double) {
    val words: MutableList<Word> = mutableListOf()
    val spaceSize = spaceWidth * fontSize
    var length: Double = 0.0
        private set

    fun attemptAddWord(word: Word): Boolean {
        var addLength: Double = word.width
        addLength += if (words.isEmpty()) 0.0 else spaceSize
        return if (length + addLength <= maxLength) {
            words.add(word)
            length += addLength
            true
        } else false
    }
}