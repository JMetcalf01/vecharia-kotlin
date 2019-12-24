package vecharia.inventory.weapons

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