package vecharia.lwjgl3

object Garbage {
    private val disposables: MutableList<Disposable> = mutableListOf()

    fun dispose(disposable: Disposable) = disposables.add(disposable)

    fun dispose() = disposables.forEach { it.dispose() }
}

interface Disposable {
    fun dispose()
}