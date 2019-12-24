package vecharia.entity

import vecharia.effect.Effect
import vecharia.inventory.Inventory

/**
 * Any Entity in the game that has a name and health,
 * which includes the player, the enemies, and the traders.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the Entity
 * @param maxHealth the max health of the Entity
 */
abstract class Entity(val name: String, private val maxHealth: Int, val race: Race, val inventory: Inventory?) {
    var effects: MutableList<Effect> = mutableListOf()

    enum class Race {
        HUMAN, ELF, DWARF
    }
}

