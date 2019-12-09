package vecharia.util

/**
 * An implementation of a queue.
 *
 * @author Jonathan Metcalf
 * @since Version 1.0
 */
class SimpleQueue<E> {
    private val elements: MutableList<E> = mutableListOf()

    /**
     * Adds an element to the queue.
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     *
     * @param e the element to add
     */
    fun push(e: E) {
        elements.add(e)
    }

    /**
     * Removes and returns the first element of the queue.
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     *
     * @return the first element, or null if the queue is empty
     */
    fun pop(): E? {
        if (elements.size == 0) return null
        return elements.removeAt(0)
    }

    /**
     * Returns the first element of the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @return the first element, or null if the queue is empty
     */
    fun peek(): E? {
        if (elements.size == 0) return null
        return elements[0]
    }

    /**
     * Clears the queue.
     *
     * @author Jonathan Metcalf
     * @since Version 1.0
     */
    fun clear() {
        elements.clear()
    }
}