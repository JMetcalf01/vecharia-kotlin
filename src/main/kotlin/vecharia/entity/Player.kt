package vecharia.entity

import vecharia.inventory.Inventory
import kotlin.math.ln
import kotlin.math.pow

/**
 * The player class, which holds the information about the player.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the player
 * @param maxHealth the maxHealth of the player
 * @param inventory the starting inventory of the player
 */
class Player private constructor(
    name: String,
    maxHealth: Int,
    race: Race,
    val pclass: Class,
    inventory: Inventory
) : Entity(name, maxHealth, race, inventory) {

    // Constants
    private val totalLevels: Int = 40
    private val firstXPLevel: Int = 10
    private val lastXPLevel: Int = 20000
    private val b: Double = ln(1.0 * lastXPLevel / firstXPLevel) / (totalLevels - 1)
    private val a: Double = 1.0 * firstXPLevel / (kotlin.math.exp(b) - 1.0)

    // figure this out later
    var mana: Int = 0
    private var xp: Int = 0

    /**
     * Adds a certain amount of xp to the player.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param add the amount of xp to be added
     */
    fun addXP(add: Int) {
        xp += add
    }

    /**
     * Returns the xp required given the level
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param level the level
     * @return the current level of the player
     */
    private fun getLevelHelper(level: Int): Int {
        val x = (a * kotlin.math.exp(b * level)).toInt()
        val y = 10.0.pow((ln(x.toDouble()) / ln(10.toDouble()) - 2.2))
        return ((x / y) * y).toInt()
    }

    /**
     * Returns the level of the player given the total
     * amount of xp the player has.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @return the level of the player
     */
    fun getLevel(): Int {
        var tempXP = xp
        var tempLevel = 1
        while (tempXP > 0) {
            tempXP -= getLevelHelper(tempLevel++)
            println("Level: ${tempLevel - 1}, XP: ${getLevelHelper(tempLevel - 1)}")
        }
        return tempLevel - 2
    }

    /**
     * Builder class for the Player.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     */
    class Builder {
        var name: String? = null
        var maxHealth: Int? = null
        var race: Race? = null
        var pclass: Class? = null
        var startingInventory: Inventory? = null

        fun build() = Player(name!!, maxHealth!!, race!!, pclass!!, startingInventory!!)
    }

    /**
     * An enum representation of possible jobs a player can have.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     */
    enum class Class {
        MELEE, RANGED, MAGIC
    }
}