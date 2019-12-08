package vecharia.util

import vecharia.Vecharia

/**
 * Functional interface for classes that can tick.
 *
 * @author Matt Worzala
 * @since 1.1
 */
@FunctionalInterface
interface Tickable {
    fun tick(game: Vecharia, frame: Int)
}