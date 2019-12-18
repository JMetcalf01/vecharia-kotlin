package com.mattworzala.nbt

import java.io.Serializable


class DataCompound(private val valueMap: MutableMap<String, Serializable> = HashMap()) : Serializable, Iterable<Serializable> {
    private val serialVersionUid = 3964207276467494972L

    val names: Array<String> get() = valueMap.keys.toTypedArray()
    val values: Array<Serializable> get() = valueMap.values.toTypedArray()

    val size: Int get() = valueMap.size
    val isEmpty: Boolean get() = valueMap.isEmpty()

    fun setValue(name: String, data: Serializable) = valueMap.put(name, data)

    fun getValue(name: String): Serializable? = valueMap[name]

    fun contains(name: String): Boolean = valueMap.containsKey(name)

    fun contains(data: Serializable) = valueMap.containsValue(data)

    fun remove(name: String) = valueMap.remove(name)

    fun empty() = valueMap.clear()

    fun forEach(action: (String, Serializable) -> Unit) = valueMap.forEach(action)

    fun replace(name: String, data: Serializable): Serializable? = valueMap.replace(name, data)

    fun getByte(name: String): Byte {
        val value = valueMap[name]
        return if (value != null && value is Byte) value else 0
    }

    fun getShort(name: String): Short? {
        val value = valueMap[name];
        return if (value != null && value is Short) value else null
    }

    fun getLong(name: String): Long? {
        val value = valueMap[name];
        return if (value != null && value is Long) value else null
    }

    fun getInt(name: String): Int? {
        val value = valueMap[name];
        return if (value != null && value is Int) value else null
    }

    fun getFloat(name: String): Float? {
        val value = valueMap[name];
        return if (value != null && value is Float) value else null
    }

    fun getDouble(name: String): Double? {
        val value = valueMap[name];
        return if (value != null && value is Double) value else null
    }

    fun getString(name: String): String? {
        val value = valueMap[name]
        return if (value != null && value is String) value else null
    }

    fun getBoolean(name: String): Boolean? {
        val value = valueMap[name]
        return if (value != null && value is Boolean) value else null
    }

    fun getBytes(name: String): Array<Byte>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Byte> else null
    }

    fun getShorts(name: String): Array<Short>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Short> else null
    }

    fun getLongs(name: String): Array<Long>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Long> else null
    }

    fun getInts(name: String): Array<Int>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Int> else null
    }

    fun getFloats(name: String): Array<Float>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Float> else null
    }

    fun getDoubles(name: String): Array<Double>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<Double> else null
    }

    fun getStrings(name: String): Array<String>? {
        val value = valueMap[name];
        return if (value != null && value is Array<*>) value as Array<String> else null
    }

    fun <T> getList(name: String): MutableList<T>? {
        val value = valueMap[name]
        return if (value != null && value is MutableList<*>) value as MutableList<T> else null
    }

    fun getDataCompound(name: String): DataCompound? {
        val value = valueMap[name]
        return if (value != null && value is DataCompound) value else null
    }

    operator fun set(name: String, data: Serializable) = setValue(name, data)

    override fun iterator(): Iterator<Serializable> = valueMap.values.iterator()

    fun clone(): DataCompound = DataCompound(HashMap(valueMap))

    override fun toString(): String = "DataCompound [valueMap=$valueMap]"

    override fun hashCode(): Int = 31 * 1 + valueMap.hashCode()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataCompound

        if (valueMap != other.valueMap) return false

        return true
    }


}