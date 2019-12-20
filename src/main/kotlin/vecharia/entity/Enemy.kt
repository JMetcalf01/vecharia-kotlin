package vecharia.entity

/**
 * The enemy class, which holds information about a specific enemy.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the enemy
 * @param maxHealth the max health of the enemy
 */
abstract class Enemy(name: String, maxHealth: Int, race: Race) : Entity(name, maxHealth, race, null) {

    // todo implement all of these variables
    val fleeBaseChance: Double = 1.0
    val pronouns: String = "he/his"
    val level: Int = 1
}