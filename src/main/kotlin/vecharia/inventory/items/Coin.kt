package vecharia.inventory.items

import vecharia.inventory.Item

class Coin(count: Int) : Item("Gold Coin", amount = count, cost = 1, maxStack = Int.MAX_VALUE)