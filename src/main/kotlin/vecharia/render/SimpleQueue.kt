package vecharia.render

class SimpleQueue<E> {
    private val elements: MutableList<E> = mutableListOf()

    fun push(e: E) {
        elements.add(e)
    }

    fun pop(): E? {
        if (elements.size == 0) return null
        return elements.removeAt(0);
    }

    fun clear() {
        elements.clear()
    }
}