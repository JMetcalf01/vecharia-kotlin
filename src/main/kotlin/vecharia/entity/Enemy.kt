package vecharia.entity

import vecharia.inventory.Inventory

/**
 * The enemy class, which holds information about a specific enemy.
 * // todo add class and race
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the enemy
 * @param maxHealth the max health of the enemy
 */
abstract class Enemy(name: String, override val maxHealth: Int) : Entity(name, maxHealth) {

    // todo implement all of these variables
    val fleeBaseChance: Double = 1.0
    val pronouns: String = "he/his"
    val level: Int = 1

}