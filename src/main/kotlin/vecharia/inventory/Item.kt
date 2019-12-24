package vecharia.inventory

import java.lang.IllegalStateException

/**
 * An item which can be acquired and put into an inventory.
 *
 * @author Matt Worzala
 * @since 1.0
 *
 * @param name the name of the item
 * @param amount the current stack size of the item
 * @param cost the cost of the item (used when buying and selling)
 * @param maxStack the maximum stack size of the item
 * @param visible whether the item is visible in the inventory
 */
abstract class Item(
    private val name: String,
    amount: Int = 1,
    private val cost: Int = -1,
    private val maxStack: Int = 99,
    private val visible: Boolean = true
) {
    var count: Int = amount
        set(value) {
            if (value < maxStack) field = value
            else throw IllegalStateException("cannot stack above max")
        }

    /**
     * Returns whether this item equals another item.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param other the other item to check
     * @return whether they equal each other
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (name != other.name) return false
        if (cost != other.cost) return false
        if (maxStack != other.maxStack) return false
        if (visible != other.visible) return false

        return true
    }

    /**
     * The new hashcode for items.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @return the hashcode of the item
     */
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + cost
        result = 31 * result + maxStack
        result = 31 * result + visible.hashCode()
        return result
    }


}