package vecharia.inventory

/**
 * An inventory holding system for players, enemies, and traders.
 *
 * @author Jonathan Metcalf
 * @since 1.5
 */
class Inventory {

    private var inventory: MutableList<Item> = mutableListOf()

    /**
     * Adds an arbitrary amount of items to the inventory.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param items the items to add
     */
    fun add(vararg items: Item) {
        for (item in items) {
            if (inventory.contains(item)) {
                val temp = inventory.get(item)
                if (temp != null)
                    temp.count += item.count
            } else {
                inventory.add(item)
            }
        }
    }

    /**
     * Adds an arbitrary amount of one item to the inventory.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param item the item to be added
     * @param count the amount of the item to be added
     */
    fun add(item: Item, count: Int) {
        item.count = count
        add(item)
    }

    /**
     * Returns whether a certain item is contained in the inventory.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param item the item to be checked
     */
    fun contains(item: Item): Boolean {
        return inventory.contains(item)
    }

    /**
     * Removes a certain item from the inventory.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param item the item to remove
     */
    fun remove(item: Item) {
        inventory.remove(item)
    }

    /**
     * Returns the size of the inventory
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @return the size of the inventory
     */
    fun size(): Int {
        return inventory.size
    }
}

/**
 * Adds a get(item) function to MutableList
 *
 * @author Jonathan Metcalf
 * @since 1.5
 *
 * @param item the item to check
 * @return the same item in the list, or null
 */
private fun <E> MutableList<E>.get(item: E): E? {
    for (element in this) {
        if (item!! == element) {
            return element
        }
    }
    return null
}
