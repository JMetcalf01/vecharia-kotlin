package vecharia.render

/**
 * An implementation of a queue
 *
 * @author Jonathan Metcalf
 * @since Version 1.0
 */
class SimpleQueue<E> {
    private val elements: MutableList<E> = mutableListOf()

    /**
     * Adds an element to the queue
     */
    fun push(e: E) {
        elements.add(e)
    }

    fun pop(): E? {
        if (elements.size == 0) return null
        return elements.removeAt(0)
    }

    fun clear() {
        elements.clear()
    }
}