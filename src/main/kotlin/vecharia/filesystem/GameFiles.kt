package vecharia.filesystem

import vecharia.lwjgl3.render.Texture
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object GameFiles {
    internal val root: Path = Paths.get(".") //todo this should come work an argument passed eventually.

    fun get(path: String): GameFile {
        if (!Files.exists(root.resolve(path)))
            throw FileNotFoundException("Cannot locate file $path!")
        return GameFile(path)
    }
}

class GameFile internal constructor(val path: String) {

    fun location(): Path = GameFiles.root.resolve(path)

    fun absolute(): String = location().toString()

    fun text(): String {
        val file = StringBuilder()
        Files.newBufferedReader(location()).use {
            var line: String? = null
            while ({ line = it.readLine(); line }() != null)
                file.append(line)
        }
        return file.toString().trim()
    }

    fun lines(): List<String> = text().split("\n")

    fun texture(): Texture = Texture.load(absolute())

    override fun toString(): String = path.substring(path.lastIndexOf('/')) //todo test me!
}