package vecharia.event

fun <T> event() = SetEvent<T>()
fun <T> namedEvent() = MapEvent<T>()