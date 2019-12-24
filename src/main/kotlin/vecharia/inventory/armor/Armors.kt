package vecharia.inventory.armor

object Armors {
    val LEATHER_ARMOR = Armor.Builder().name("Leather Armor").cost(200).defense(10).durability(50).build()
    val CHAINMAIL_ARMOR = Armor.Builder().name("Chainmail Armor").cost(500).defense(20).durability(75).build()
    val PLATE_ARMOR = Armor.Builder().name("Plate Armor").cost(1000).defense(40).durability(150).build()
    val MITHRIL_ARMOR = Armor.Builder().name("Mithril Armor").cost(15_000).defense(90).durability(500).build()
}