package vecharia.inventory

import vecharia.effect.Effect
import vecharia.entity.Player

class Armor(
    name: String,
    cost: Int,
    val baseDefense: Int,
    private val startingDurability: Int,
    val effects: List<Effect>,
    val sellable: Boolean
) : Item(name, 1, cost, 1, true), Durable, Equippable {

    private var currentDurability: Int = startingDurability

    /**
     * Gets the current durability of the item.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the current durability of the item
     */
    override fun getDurability(): Int {
        return currentDurability
    }

    /**
     * Gets the maximum durability of the item.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the maximum durability of the item
     */
    override fun getMaxDurability(): Int {
        return startingDurability
    }

    /**
     * Removes the given amount from the durability of the item, breaking it if applicable.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param amount the amount of durability to remove.
     * @return the new durability of the item
     */
    override fun removeDurability(amount: Int): Int {
        if (currentDurability < amount) {
            currentDurability = 0
            breakItem()
        } else currentDurability -= amount
        return currentDurability
    }

    /**
     * Equip the current item to the given equipment slot, false will be returned
     *     if the item cannot be equipped to the target slot.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param slot the slot to equip to
     * @return whether the item was equipped to the slot
     */
    override fun equip(slot: EquipmentSlot, player: Player): Boolean {
        if (this.getSlot() != slot) return false
        player.hand = this
        return true
    }

    /**
     * Get the slot which this item may be equipped to.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @return the slot which this item may be equipped.
     */
    override fun getSlot(): EquipmentSlot {
        return EquipmentSlot.ARMOR
    }

    /**
     * TODO
     *
     */
    private fun breakItem() {
        TODO("not implemented")
    }

    /**
     * Builder class for armor.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     */
    class Builder {
        private var name: String? = null
        private var cost: Int? = null
        private var baseDefense: Int? = null
        private var durability: Int? = null
        private var effects: MutableList<Effect> = mutableListOf()
        private var sellable: Boolean = true

        fun name(name: String) = apply { this.name = name }
        fun cost(cost: Int) = apply { this.cost = cost }
        fun defense(defense: Int) = apply { this.baseDefense = defense }
        fun durability(durability: Int) = apply { this.durability = durability }
        fun effects(effects: List<Effect>) = apply { this.effects = effects as MutableList<Effect> }
        fun disableSelling() = apply { sellable = false }

        fun build() =
            Armor(name!!, cost!!, baseDefense!!, durability!!, effects, sellable)
    }

    companion object Armors {
        val LEATHER_ARMOR = Builder().name("Leather Armor").cost(200).defense(10).durability(50).build()
        val CHAINMAIL_ARMOR = Builder().name("Chainmail Armor").cost(500).defense(20).durability(75).build()
        val PLATE_ARMOR = Builder().name("Plate Armor").cost(1000).defense(40).durability(150).build()
        val MITHRIL_ARMOR = Builder().name("Mithril Armor").cost(15_000).defense(90).durability(500).build()
    }
}