package vecharia.entity

import vecharia.Vecharia
import vecharia.inventory.Inventory
import vecharia.inventory.Item
import vecharia.menu.Menu

class Trader(
    name: String,
    health: Int,
    race: Race,
    inventory: Inventory,
    val ratio: Double,
    val job: Jobs,
    val game: Vecharia
) : Entity(name, health, race, inventory) {

    var buying: Boolean = true

    fun greet(player: Player) {
        Menu.basic(
            game,
            "\"Hello, ${player.name}. Is there anything you wish to buy or sell today?",
            "Purchase Supplies",
            "Sell Supplies",
            "Leave"
        ).then { option ->
            when(option) {
                0 -> {
                    buySupplies(player)
                }
                1 -> {
                    sellSupplies(player)
                }
                2 -> {
                    game.printer.clear()
                    game.printer += "You leave the $job."
                }
            }
        }
    }

    private fun buySupplies(player: Player) {
        TODO("not implemented")
    }

    private fun sellSupplies(player: Player) {
        TODO("not implemented")
    }


    class Builder(val game: Vecharia) {
        private var name: String? = null
        private var health: Int? = null
        private var race: Race? = null
        private var inventory: Inventory = Inventory()
        private var ratio: Double? = null
        private var job: Jobs? = null

        fun name(name: String) = apply { this.name = name }
        fun health(health: Int) = apply { this.health = health }
        fun race(race: Race) = apply { this.race = race }
        fun items(vararg items: Item) = apply { this.inventory.add(*items) }
        fun ratio(ratio: Double) = apply { this.ratio = ratio }
        fun job(job: Jobs) = apply { this.job = job }

        fun build() = Trader(name!!, health!!, race!!, inventory, ratio!!, job!!, game)
    }

    companion object Traders {

    }

    enum class Jobs {
        armorer, trader
    }
}
