package com.mattworzala.nbt

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object Nbt {
    fun readCompound(file: String): DataCompound = readCompound(Paths.get(".").resolve(file))

    private fun readCompound(path: Path): DataCompound {
        Files.newInputStream(path).use { fileInput ->
            ObjectInputStream(fileInput).use {
                return it.readObject() as DataCompound
            }
        }
    }

    fun writeCompound(data: DataCompound, file: String) = writeCompound(data, Paths.get(".").resolve(file))

    private fun writeCompound(data: DataCompound, path: Path) {
        Files.newOutputStream(path).use { fileOutput ->
            ObjectOutputStream(fileOutput).use { it.writeObject(data) }
        }
    }
}
