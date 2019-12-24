package vecharia.inventory.weapons

import vecharia.inventory.Item

/**
 * Ammunition are items.
 *
 * @author Jonathan Metcalf
 * @since 1.5
 *
 * @param name the name of the ammunition
 * @param amount the amount of the ammunition
 * @param cost the cost of the ammunition
 */
class Ammunition(name: String, amount: Int, cost: Int, val damage: Int): Item(name, amount, cost, 99, true) {

    /**
     * Builder class for ammunition.
     *
     * @author Jonathan Metcalf
     * @since 1.5
     */
    class Builder() {
        private var name: String? = null
        private var amount: Int? = null
        private var cost: Int? = null
        private var damage: Int? = null

        fun name(name: String) = apply { this.name = name }
        fun amount(amount: Int) = apply { this.amount = amount }
        fun cost(cost: Int) = apply { this.cost = cost }
        fun damage(damage: Int) = apply { this.damage = damage }

        fun build() = Ammunition(name!!, amount!!, cost!!, damage!!)
    }
}