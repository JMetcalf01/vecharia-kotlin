package vecharia.event

abstract class AbstractEvent<T> : Event<T> {
    override fun plusAssign(handler: (T) -> Unit) {
        add(handler)
    }

    override fun minusAssign(handler: (T) -> Unit) {
        remove(handler)
    }

    override fun invoke(data: T) = forEach { it(data) }
}