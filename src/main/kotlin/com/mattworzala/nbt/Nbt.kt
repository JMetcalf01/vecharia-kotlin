package com.mattworzala.nbt

import java.nio.file.Path
import java.nio.file.Paths

object Nbt {
    fun readCompound(file: String): DataCompound {
        return readCompound(Paths.get(".").resolve(file))
    }

    fun readCompound(path: Path): DataCompound {
//        with(val oStream = ObjectInputStream())
        return DataCompound()
    }
}