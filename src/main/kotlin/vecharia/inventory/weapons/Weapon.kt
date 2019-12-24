package vecharia.inventory.weapons

import vecharia.effect.Effect
import vecharia.entity.Player
import vecharia.inventory.Durable
import vecharia.inventory.EquipmentSlot
import vecharia.inventory.Equippable
import vecharia.inventory.Item

class Weapon(
    name: String,
    cost: Int,
    val baseDamage: Int,
    private val startingDurability: Int?,
    val ammo: Item?,
    val effects: List<Effect>,
    val sellable: Boolean
) : Item(name, 1, cost, 1, true),
    Durable, Equippable {

    private var currentDurability: Int? = startingDurability

    /**
     * Gets the current durability of the item.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @return the current durability of the item
     */
    override fun getDurability(): Int {
        if (currentDurability == null) return -1
        return currentDurability as Int
    }

    /**
     * Gets the maximum durability of the item.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @return the maximum durability of the item
     */
    override fun getMaxDurability(): Int {
        if (currentDurability == null) return -1
        return startingDurability as Int
    }

    /**
     * Removes the given amount from the durability of the item, breaking it if applicable.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     *
     * @param amount the amount of durability to remove.
     * @return the new durability of the item
     */
    override fun removeDurability(amount: Int): Int {
        if (currentDurability == null) return -1

        if (currentDurability!! < amount) {
            currentDurability = 0
            breakItem()
        } else currentDurability = currentDurability!! - amount
        return currentDurability!!
    }

    /**
     * Equip the current item to the given equipment slot, false will be returned
     *     if the item cannot be equipped to the target slot.
     *
     * @author Jonathan Metcalf
     * @since 1.5
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
        return EquipmentSlot.HAND
    }

    /**
     * TODO
     *
     */
    private fun breakItem() {
        TODO("not implemented")
    }


    /**
     * Builder class for weapons.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     */
    class Builder() {
        private var name: String? = null
        private var cost: Int? = null
        private var baseDamage: Int? = null
        private var durability: Int? = null
        private var ammo: Item? = null
        private var effects: MutableList<Effect> = mutableListOf()
        private var sellable: Boolean = true

        fun name(name: String) = apply { this.name = name }
        fun cost(cost: Int) = apply { this.cost = cost }
        fun damage(damage: Int) = apply { this.baseDamage = damage }
        fun durability(durability: Int) = apply { this.durability = durability }
        fun ammo(item: Item?) = apply { this.ammo = item }
        fun effects(effects: List<Effect>) = apply { this.effects = effects as MutableList<Effect> }
        fun disableSelling() = apply { sellable = false }

        fun build() = Weapon(
            name!!,
            cost!!,
            baseDamage!!,
            durability,
            ammo,
            effects,
            sellable
        )
    }

    /**
     * This holds the list of all the weapons in the game.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     */
    object Weapons {
        // Ammunition
        val STEEL_ARROW = Ammunition.Builder().name("Steel Arrow").cost(1).damage(3).build()
        val LEAD_BALL = Ammunition.Builder().name("Lead Ball").cost(2).damage(5).build()

        // Melee
        val BRONZE_SWORD = Weapon.Builder().name("Bronze Sword").cost(20).damage(10).durability(50).build()
        val SMALL_DAGGER = Weapon.Builder().name("Small Dagger").cost(5).damage(3).durability(100).build()
        val RAPIER = Weapon.Builder().name("Rapier").cost(15).damage(9).durability(45).build()
        val BATTLEAXE = Weapon.Builder().name("Battleaxe").cost(30).damage(12).durability(100).build()

        // Ranged
        val LONGBOW = Weapon.Builder().name("Longbow").cost(30).damage(7).durability(40).ammo(STEEL_ARROW).build()
        val SLING = Weapon.Builder().name("Leather Sling").cost(12).damage(6).durability(60).ammo(LEAD_BALL).build()

        // Staffs
        val COOL_STICK = Weapon.Builder().name("Cool Stick").cost(1).damage(10).build()
        val OAK_STAFF = Weapon.Builder().name("Oak Staff").cost(15).damage(10).build()
    }
}