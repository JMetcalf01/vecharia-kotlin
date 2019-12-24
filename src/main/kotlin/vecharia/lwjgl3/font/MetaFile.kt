package vecharia.lwjgl3.font

import vecharia.lwjgl3.Window
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

const val PAD_TOP = 0
const val PAD_LEFT = 1
const val PAD_BOTTOM = 2
const val PAD_RIGHT = 3

const val DESIRED_PADDING = 8

const val SPLITTER = " "
const val NUMBER_SEPARATOR = ","

class MetaFile(window: Window, file: File) {
    var spaceWidth: Double = 0.0
        private set

    private val metaData: MutableMap<Int, Character> = mutableMapOf()
    private val values: MutableMap<String, String> = mutableMapOf()
    private lateinit var reader: BufferedReader

    private val aspectRatio: Double
    private var verticalPerPixelSize: Double = 0.0
    private var horizontalPerPixelSize: Double = 0.0
    private var padding: List<Int> = listOf()
    private var paddingWidth: Int = 0
    private var paddingHeight: Int = 0

    init {
        aspectRatio = window.ratio.toDouble() //todo this needs to update with a window callback.
        open(file)
        loadPaddingData()
        loadLineSizes()
        val imageWidth = getValueOfVariable("scaleW")
        loadCharacterData(imageWidth)
        close()
    }

    fun getCharacter(ascii: Int): Character = metaData[ascii] ?: throw IllegalArgumentException("ASCII code $ascii is out of valid range!")

    private fun getValueOfVariable(variable: String): Int = values[variable]?.toInt() ?: throw IllegalStateException("Unable to get value of $variable!")

    private fun getValuesOfVariable(variable: String): List<Int> {
        val numbers = values[variable]?.split(NUMBER_SEPARATOR) ?: throw IllegalStateException("Unable to get value of $variable!")
        return numbers.map { it.toInt() }
    }

    private fun open(file: File) { reader = BufferedReader(FileReader(file)) }

    private fun close() = reader.close()

    private fun processNextLine(): Boolean {
        values.clear()
        var line: String? = null
        try {
            line = reader.readLine()
        } catch (_: IOException) {}
        if (line == null)
            return false
        for (part in line.split(SPLITTER)) {
            val vals = part.split("=")
            if (vals.size == 2)
                values[vals[0]] = vals[1]
        }
        return true
    }

    private fun loadPaddingData() {
        processNextLine()
        padding = getValuesOfVariable("padding")
        paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT]
        paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM]
    }

    private fun loadLineSizes() {
        processNextLine()
        val lineHeightPixels = getValueOfVariable("lineHeight") - paddingHeight
        verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT / lineHeightPixels.toDouble()
        horizontalPerPixelSize = verticalPerPixelSize / aspectRatio
    }

    private fun loadCharacterData(imageWidth: Int) {
        processNextLine()
        processNextLine()
        while (processNextLine()) {
            val c = loadCharacter(imageWidth)
            if (c != null)
                metaData[c.id] = c
        }
    }

    private fun loadCharacter(imageWidth: Int): Character? {
        val id = getValueOfVariable("id")
        if (id == TextMeshCreator.SPACE_ASCII) {
            spaceWidth = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize
            return null
        }
        val xTex = (getValueOfVariable("x") + (padding[PAD_LEFT] - DESIRED_PADDING)).toDouble() / imageWidth
        val yTex = (getValueOfVariable("y") + (padding[PAD_TOP] - DESIRED_PADDING)).toDouble() / imageWidth
        val width = getValueOfVariable("width") - (paddingWidth - (2 * DESIRED_PADDING))
        val height = getValueOfVariable("height") - (paddingHeight - (2 * DESIRED_PADDING))
        val quadWidth = width * horizontalPerPixelSize
        val quadHeight = height * verticalPerPixelSize
        val xTexSize = width.toDouble() / imageWidth
        val yTexSize = height.toDouble() / imageWidth
        val xOff = (getValueOfVariable("xoffset") + padding[PAD_LEFT] - DESIRED_PADDING) * horizontalPerPixelSize
        val yOff = (getValueOfVariable("yoffset") + padding[PAD_TOP] - DESIRED_PADDING) * verticalPerPixelSize
        val xAdvance = (getValueOfVariable("xadvance") - paddingWidth) * horizontalPerPixelSize
        return Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance)
    }
}