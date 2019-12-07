package vecharia.util

import vecharia.Vecharia

@FunctionalInterface
interface Tickable {
    fun tick(game: Vecharia)
}