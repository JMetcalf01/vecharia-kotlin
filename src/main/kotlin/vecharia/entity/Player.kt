package vecharia.entity

import vecharia.quest.Quest
import kotlin.math.ln
import kotlin.math.pow

/**
 * The player class, which holds the information about the player.
 * // todo add class and race
 *
 * @author Jonathan Metcalf
 * @since 1.0
 *
 * @param name the name of the player
 * @param maxHealth the maxHealth of the player
 */
class Player(name: String, maxHealth: Int) : Entity(name, maxHealth) {

    // Constants
    private val totalLevels: Int = 40
    private val firstXPLevel: Int = 10
    private val lastXPLevel: Int = 20000
    private val b: Double = ln(1.0 * lastXPLevel / firstXPLevel) / (totalLevels - 1)
    private val a: Double = 1.0 * firstXPLevel / (kotlin.math.exp(b) - 1.0)

    // Instance Variables
    var xp: Int = 0
    var quest: Quest? = null
    var possibleQuests: MutableList<Quest> = mutableListOf()
    var mana: Int = 0

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
}