package vecharia.entity

import vecharia.effect.Effect
import vecharia.inventory.Inventory

/**
 * Any Entity in the game that has a name and health,
 * which includes the player, the enemies, and the traders.
 * todo add race and class to the constructor as val's
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the Entity
 * @param maxHealth the max health of the Entity
 */
abstract class Entity(private val name: String?, open val maxHealth: Int?) {
    var health: Int? = this.maxHealth
    val inventory: Inventory = Inventory()
    var effects: MutableList<Effect> = mutableListOf()
}
