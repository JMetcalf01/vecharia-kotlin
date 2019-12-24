package vecharia.util

/**
 * Represents a value which will be returned eventually.
 * Thanks JavaScript
 *
 * @author Matt Worzala
 * @since 1.3
 *
 * @param run whether to invoke this function immediately or not, for internal use only
 * @param func the resolution function
 *
 * @constructor invokes the resolution function
 */
class Promise<T>(run: Boolean = true, private val func: ((T) -> Unit) -> Unit) {
    companion object {
        /**
         * Runs a set of promises, with a guarantee that they will be executed in order, and returns the result of the final promise.
         *
         * @author Matt Worzala
         * @since 1.3
         *
         * @param promises a set of promises to execute in order
         * @return a promise, resolved when the final promise has resolved
         */
        fun <T> sequential(vararg promises: Promise<T>): Promise<T> = Promise { resolve ->
            val remaining = promises.asList().toMutableList()
            var cb: (T) -> Unit = {}
            cb = {
                println("Running next: ${remaining.isNotEmpty()}")
                if (remaining.isEmpty())
                    resolve(it)
                else {
                    val current = remaining[0]
                    remaining.removeAt(0)
                    current.then(cb)
                }
            }
            val current = remaining[0]
            remaining.removeAt(0)
            current.then(cb)
        }

//        fun all(vararg promises: Promise<*>)= TODO()
    }

    private var value: T? = null
    private var next: Promise<*>? = null
    private var end: ((T) -> Unit)? = null

    init {
        if (run) invoke()
    }

    /**
     * Add a transformation function to another promise, possibly of a different type.
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param func the resolution function of the new promise
     * @return the new promise
     */
    fun <T2> then(func: ((T2) -> Unit, T) -> Unit): Promise<T2> {
        val temp = Promise<T2>(false) {
            val tempValue = value
            if (tempValue != null)
                func(it, tempValue)
        }
        next = temp
        if (value != null) temp.invoke()
        return temp
    }

    /**
     * Add a terminating resolution function
     *
     * @author Matt Worzala
     * @since 1.3
     *
     * @param func the resolution function
     */
    fun then(func: (T) -> Unit) {
        end = func
        val temp = value
        if (temp != null)
            end?.invoke(temp)
    }

    /**
     * Invoke the promise.
     * Called by another promise, when it has been completed.
     *
     * @author Matt Worzala
     * @since 1.3
     */
    private fun invoke() {
        func {
            value = it
            if (next != null)
                next?.invoke()
            if (end != null)
                end?.invoke(it)
        }
    }
}