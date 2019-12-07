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
abstract class Item(val name: String, amount: Int = 1, val cost: Int = -1, val maxStack: Int = 99, val visible: Boolean = true) {
    var count: Int = amount
        set(value) {
            if (value < maxStack) field = value
            else throw IllegalStateException("cannot stack above max")
        }
}