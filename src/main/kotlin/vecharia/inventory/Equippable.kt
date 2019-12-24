package vecharia.inventory

import vecharia.entity.Player

/**
 * Represents an item which can be equipped to an Entity.
 *
 * @author Matt Worzala
 * @since 1.0
 *
 * @see vecharia.entity.Entity
 */
interface Equippable {

    /**
     * Equip the current item to the given equipment slot, false will be returned
     *     if the item cannot be equipped to the target slot.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param slot the slot to equip to
     * @param player the player to check
     * @return whether the item was equipped to the slot
     */
    fun equip(slot: EquipmentSlot, player: Player): Boolean

    /**
     * Get the slot which this item may be equipped to.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the slot which this item may be equipped.
     */
    fun getSlot(): EquipmentSlot
}

/**
 * All valid equipment slots for the player.
 */
enum class EquipmentSlot {
    HAND, ARMOR
}