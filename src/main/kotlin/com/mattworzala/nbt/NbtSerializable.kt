package com.mattworzala.nbt

/**
 *
 *
 * @author Matt Worzala
 */
interface NbtSerializable {
    fun write(tag: DataCompound)

    fun read(tag: DataCompound)
}