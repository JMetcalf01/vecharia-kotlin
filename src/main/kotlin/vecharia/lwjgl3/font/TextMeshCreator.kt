package vecharia.lwjgl3.font

import vecharia.lwjgl3.Window
import java.io.File

class TextMeshCreator(window: Window, metaFile: File) {
    companion object {
        const val LINE_HEIGHT: Double = 0.03
        const val SPACE_ASCII: Int = 32
    }

    val metaData: MetaFile = MetaFile(window, metaFile)

    fun createTextMesh(text: GUIText): TextMeshData = createQuadVertices(text, createStructure(text))

    private fun createStructure(text: GUIText): MutableList<Line> {
        val lines = mutableListOf<Line>()
        var currentLine = Line(text.maxLineLength.toDouble(), metaData.spaceWidth, text.fontSize.toDouble())
        var currentWord = Word(text.fontSize.toDouble())
        for (char in text.text) {
            val ascii = char.toInt()
            if (ascii == SPACE_ASCII) {
                val added = currentLine.attemptAddWord(currentWord)
                if (!added) {
                    lines.add(currentLine)
                    currentLine = Line(metaData.spaceWidth, text.fontSize.toDouble(), text.maxLineLength.toDouble())
                    currentLine.attemptAddWord(currentWord)
                }
                currentWord = Word(text.fontSize.toDouble())
                continue
            }
            currentWord.add(metaData.getCharacter(ascii))
        }
        completeStructure(lines, currentLine, currentWord, text)
        return lines
    }

    private fun completeStructure(lines: MutableList<Line>, currentLine: Line, currentWord: Word, text: GUIText) {
        var maybeLine = currentLine
        if (!maybeLine.attemptAddWord(currentWord)) {
            lines.add(maybeLine)
            maybeLine = Line(metaData.spaceWidth, text.fontSize.toDouble(), text.maxLineLength.toDouble())
            maybeLine.attemptAddWord(currentWord)
        }
        lines.add(maybeLine)
    }

    private fun createQuadVertices(text: GUIText, lines: MutableList<Line>): TextMeshData {
        text.lineCount = lines.size
        var cursorX: Double = 0.0
        var cursorY: Double = 0.0
        val vertices: MutableList<Float> = mutableListOf()
        val texCoords: MutableList<Float> = mutableListOf()
        for (line in lines) {
            if (text.centered)
                cursorX = (line.maxLength - line.length) / 2
            for (word in line.words) {
                for (char in word.characters) {
                    addVerticesForCharacter(cursorX, cursorY, char, text.fontSize.toDouble(), vertices)
                    addTexCoords(texCoords, char.xTexture, char.yTexture, char.xMaxTexture, char.yMaxTexture)
                    cursorX += char.xAdvance * text.fontSize
                }
                cursorX += metaData.spaceWidth * text.fontSize.toDouble()
            }
            cursorX = 0.0
            cursorY += LINE_HEIGHT * text.fontSize
        }
        return TextMeshData(vertices.toFloatArray(), texCoords.toFloatArray())
    }

    private fun addVerticesForCharacter(cursorX: Double, cursorY: Double, character: Character, fontSize: Double, vertices: MutableList<Float>) {
        val x: Double = cursorX + character.xOffset * fontSize
        val y: Double = cursorY + character.yOffset * fontSize
        val maxX: Double = x + character.xSize * fontSize
        val maxY: Double = y + character.ySize * fontSize
        val properX = 2 * x - 1
        val properY = -2 * y + 1
        val properMaxX = 2 * maxX - 1
        val properMaxY = -2 * maxY + 1
        addVertices(vertices, properX, properY, properMaxX, properMaxY)
    }

    private fun addVertices(vertices: MutableList<Float>, x: Double, y: Double, maxX: Double, maxY: Double) {
        vertices.add(x.toFloat())
        vertices.add(y.toFloat())
        vertices.add(x.toFloat())
        vertices.add(maxY.toFloat())
        vertices.add(maxX.toFloat())
        vertices.add(maxY.toFloat())
        vertices.add(maxX.toFloat())
        vertices.add(maxY.toFloat())
        vertices.add(maxX.toFloat())
        vertices.add(y.toFloat())
        vertices.add(x.toFloat())
        vertices.add(y.toFloat())
    }

    private fun addTexCoords(texCoords: MutableList<Float>, x: Double, y: Double, maxX: Double, maxY: Double) {
        texCoords.add(x.toFloat())
        texCoords.add(y.toFloat())
        texCoords.add(x.toFloat())
        texCoords.add(maxY.toFloat())
        texCoords.add(maxX.toFloat())
        texCoords.add(maxY.toFloat())
        texCoords.add(maxX.toFloat())
        texCoords.add(maxY.toFloat())
        texCoords.add(maxX.toFloat())
        texCoords.add(y.toFloat())
        texCoords.add(x.toFloat())
        texCoords.add(y.toFloat())
    }
}