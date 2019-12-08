package vecharia.inventory

/**
 * Represents an item which has durability and can be broken or repaired
 */
interface Durable {

    /**
     * Gets the current durability of the item
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the current durability of the item
     */
    fun getDurability(): Int

    /**
     * Gets the maximum durability of the item
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the maximum durability of the item
     */
    fun getMaxDurability(): Int

    /**
     * Removes the given amount from the durability of the item, breaking it if applicable.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param amount the amount of durability to remove.
     * @return the new durability of the item
     */
    fun removeDurability(amount: Int): Int
}