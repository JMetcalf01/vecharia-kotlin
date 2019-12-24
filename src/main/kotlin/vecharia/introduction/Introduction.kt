package vecharia.introduction

import vecharia.Input
import vecharia.Vecharia
import vecharia.entity.Entity
import vecharia.entity.Player
import vecharia.inventory.Inventory
import vecharia.menu.Menu
import vecharia.render.Text
import vecharia.util.Promise
import java.util.*

/**
 * This introduces the player to the world and
 * sets their name, race, class, etc.
 *
 * @author Jonathan Metcalf
 * @since 1.3
 *
 * @param game the Vecharia game instance
 */
class Introduction(val game: Vecharia) {

    private val rand: Random = Random()

    private val HUMAN_NAMES = arrayOf(
        "Elric", "Cain", "Rodmund", "Tibault", "Alvyn", "Gilbert", "Lionel", "Brennan", "Raimund", "Axel"
    )

    private val ELF_NAMES = arrayOf(
        "Afamrail", "Galaeron", "Ilbryen", "Braern", "Deldrach", "Feno", "Ayduin", "Jhaeros", "Erlathan", "Iorveth"
    )

    private val DWARF_NAMES = arrayOf(
        "Gotharldi", "Thargrim", "Brumdus", "Umtharm", "Thorik", "Dalnyl", "Ragkyl", "Hjolrigg", "Grambrek", "Belryl"
    )

    /**
     * Call this for a player to go through the introduction.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @return a promise of the player
     */
    fun introduction() = Promise<Player> { resolve ->


        getName(Player.Builder()).then { builder ->
            getRace(builder).then { builder1 ->
                getClass(builder1).then { builder2 ->
                    confirmClass(builder2).then { finalBuilder ->
                        resolve(finalBuilder.build())
                    }
                }
            }
        }
//        val builder = Player.Builder()
//        Promise.sequential(
//            getName(builder),
//            getClass(builder),
//            confirmClass(builder)
//        ).then { resolve(it.build()) }
    }

    /**
     * Gets the name of the player.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     * @return a promise of the updated builder
     */
    private fun getName(builder: Player.Builder): Promise<Player.Builder> = Promise { resolve ->

        // Initialize inventory
        builder.startingInventory = Inventory()

        // Begin printing
        game.printer.clear()
        game.printer += "You wake from a deep sleep. Rubbing your eyes blearily, you roll over and sit up."
        game.printer += Text("What's your name again?") {
            Input.readInput(2, 16).then {
                builder.name = it
                resolve(builder)
            }
        }
    }

    /**
     * Gets the race of the player.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     * @return a promise of the updated builder
     */
    private fun getRace(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer.waiting("Right. It's ${builder.name}.").then {
            game.printer.clear()

            Menu.basic(game, "What race are you?", "Human", "Elf", "Dwarf").then { option ->
                game.printer.clear()
                when (option) {
                    0 -> {
                        builder.race = Entity.Race.HUMAN
                        builder.maxHealth = 100
                        game.printer += "That's right! You are a young human in the town of Zloridge."
                        game.printer += "Zloridge is under the rule of King Henry IV."
                        game.printer += "The day to become a full-fledged adventurer has come!"
                        game.printer += "Your pay is meager, but you have managed to save up 1000 gold coins for your quest. [TODO WHEN INVENTORY IS IMPLEMENTED]"
                        // add coins to builder.inventory
                        game.printer += "If you keep forgetting basic stuff like that you're a human, maybe you should be sleeping more..."
                        game.printer += Text(
                            "You dismiss that thought from your mind and get changed, find something to eat, and leave your hut.",
                            wait = true
                        )
                        resolve(builder)
                    }
                    1 -> {
                        builder.race = Entity.Race.ELF
                        builder.maxHealth = 90
                        game.printer += "You are a young elf in the elven town of Mythmerius."
                        game.printer += "You answer to no king; Mythmerius is a democracy."
                        game.printer += "Today is the day that you choose the quest you wish to attempt."
                        game.printer += "If you keep forgetting basic stuff like the fact that you're an elf, maybe you should be sleeping more..."
                        game.printer += Text(
                            "You dismiss that thought from your mind and get changed, find something to eat, and leave your house.",
                            wait = true
                        )
                        resolve(builder)
                    }
                    2 -> {
                        builder.race = Entity.Race.DWARF
                        builder.maxHealth = 110
                        game.printer += "You are a young dwarf in the dwarven town of Mil Gurum."
                        game.printer += "Your leader is the proud King Meldal."
                        game.printer += "Today you will finally choose your quest!"
                        game.printer += "Your pay is not much, but you have managed to save up 1250 gold coins for your quest. [TODO WHEN INVENTORY IS IMPLEMENTED]"
                        // add coins to builder.inventory
                        game.printer += "How could you forget you're a dwarf?"
                        game.printer += Text(
                            "You climb out of bed, struggling to reach the floor. Why are these beds so damn high?",
                            wait = true
                        )
                        resolve(builder)
                    }
                }
            }
        }
    }

    /**
     * Gets the class based on the race of the player.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     */
    private fun getClass(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        when (builder.race) {
            Entity.Race.HUMAN -> humanClass(builder).then { resolve(it) }
            Entity.Race.ELF -> elfClass(builder).then { resolve(it) }
            Entity.Race.DWARF -> dwarfClass(builder).then { resolve(it) }
        }
    }

    /**
     * Gets the class if the player is a human.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     */
    private fun humanClass(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "As you walk past the marketplace to the castle, you hear something from over your shoulder."
        game.printer.waiting("It's your old friend, ${getFriendName(builder.race!!)}.").then {
            Menu.basic(game, "\"Hey, ${builder.name}! You decided on your job yet?\"", "Knight", "Archer", "Wizard")
                .then { option ->
                    when (option) {
                        0 -> {
                            builder.pclass = Player.Class.MELEE
                            game.printer.clear()
                            game.printer.waiting("\"Knight, huh? I always knew you preferred to be in the thick of the fighting.\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        1 -> {
                            builder.pclass = Player.Class.RANGED
                            game.printer.clear()
                            game.printer.waiting("\"An archer, huh? I always knew you liked to stay out of the fighting. Makes sense.\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        2 -> {
                            builder.pclass = Player.Class.MAGIC
                            game.printer.clear()
                            game.printer.waiting("\"A wizard, huh? I'm not surprised -- you always were good at that kinda stuff.\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                    }
                }
        }
    }

    /**
     * Gets the class if the player is an elf.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     */
    private fun elfClass(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "On your way to your mentor, you hear something from over your shoulder."
        game.printer.waiting("It's your childhood friend, ${getFriendName(builder.race!!)}.").then {
            Menu.basic(
                game,
                "\"Greetings, ${builder.name}. Have you decided on your occupation? Duelist, archer, or mage?\"",
                "Knight",
                "Archer",
                "Mage"
            )
                .then { option ->
                    when (option) {
                        0 -> {
                            builder.pclass = Player.Class.MELEE
                            game.printer.clear()
                            game.printer += "\"You're definitely quick enough on your feet to be a duelist.\""
                            game.printer.waiting("\"I like your choice.\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        1 -> {
                            builder.pclass = Player.Class.RANGED
                            game.printer.clear()
                            game.printer += "\"You don't miss a thing with those eyes...\""
                            game.printer.waiting("\"You'll be a good archer.\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        2 -> {
                            builder.pclass = Player.Class.MAGIC
                            game.printer.clear()
                            game.printer += "\"You always did have an affinity with spells.\""
                            game.printer.waiting("Good choice.")
                                .then {
                                    resolve(builder)
                                }
                        }
                    }
                }
        }
    }

    /**
     * Gets the class if the player is a dwarf.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder object
     */
    private fun dwarfClass(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "As you pass the blacksmith on your way to meet with the king, you hear a deep voice yell."
        game.printer.waiting("It's your drinking buddy, ${getFriendName(builder.race!!)}.").then {
            Menu.basic(
                game,
                "\"Sup, ${builder.name}? Gonna be a brawler or a slinger? Or what about those new sorcerers?\"",
                "Brawler",
                "Slinger",
                "Sorcerer"
            )
                .then { option ->
                    when (option) {
                        0 -> {
                            builder.pclass = Player.Class.MELEE
                            game.printer.clear()
                            game.printer.waiting("\"A fighter, eh? I'll drink to that!\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        1 -> {
                            builder.pclass = Player.Class.RANGED
                            game.printer.clear()
                            game.printer.waiting("\"A slinger, eh? Me likey!\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                        2 -> {
                            builder.pclass = Player.Class.MAGIC
                            game.printer.clear()
                            game.printer.waiting("\"A sorcerer? Alright! Spell me up some fuckin' beer!\"")
                                .then {
                                    resolve(builder)
                                }
                        }
                    }
                }
        }
    }

    /**
     * Returns a single name from the list of names based on the player's race.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param race the race of the player
     * @return the String consisting of the friend's name
     */
    private fun getFriendName(race: Entity.Race): String {
        return when (race) {
            Entity.Race.HUMAN -> HUMAN_NAMES[rand.nextInt(HUMAN_NAMES.size)]
            Entity.Race.ELF -> ELF_NAMES[rand.nextInt(ELF_NAMES.size)]
            Entity.Race.DWARF -> DWARF_NAMES[rand.nextInt(DWARF_NAMES.size)]
        }
    }

    /**
     * Confirms that the player wants the job that they chose.
     * Note that this is also based on what race they chose previously.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun confirmClass(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        when (builder.race) {
            Entity.Race.HUMAN -> humanMeeting(builder).then { resolve(it) }
            Entity.Race.ELF -> elfMeeting(builder).then { resolve(it) }
            Entity.Race.DWARF -> dwarfMeeting(builder).then { resolve(it) }
        }
    }

    /**
     * The meeting about the class for the human.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun humanMeeting(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "You walk into the courtyard of the castle, and into the throne hall."
        game.printer.waiting("The benevolent King Henry IV sits on his throne.").then {
            Menu.basic(
                game,
                "\"Greetings, ${builder.name}. You say you wish to become a ${getHumanJob(builder)}. Are you sure of this choice?\"",
                "Yes",
                "No"
            ).then { option ->
                when (option) {
                    0 -> {
                        when (builder.pclass) {
                            Player.Class.MELEE -> knightHumanText(builder).then { resolve(it) }
                            Player.Class.RANGED -> archerHumanText(builder).then { resolve(it) }
                            Player.Class.MAGIC -> wizardHumanText(builder).then { resolve(it) }
                        }
                    }
                    1 -> {
                        recheckClassHuman(builder)
                    }
                }
            }
        }
    }

    /**
     * Rechecks the class if the user is unsure of what class they want to be.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     */
    private fun recheckClassHuman(builder: Player.Builder) {
        Menu.basic(game, "\"What do you wish to be, then?\"", "Knight", "Archer", "Wizard").then { option ->
            when (option) {
                0 -> {
                    builder.pclass = Player.Class.MELEE
                    knightHumanText(builder)
                }
                1 -> {
                    builder.pclass = Player.Class.RANGED
                    archerHumanText(builder)
                }
                2 -> {
                    builder.pclass = Player.Class.MAGIC
                    wizardHumanText(builder)
                }
            }
        }
    }

    /**
     * The final meeting with the king as a knight.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun knightHumanText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"You've decided to become a warrior.\""
        game.printer += "\"To aid you on your quests, I will grant you a sword.\""
        game.printer += "He hands you a sturdy bronze broadsword. [TODO WHEN INVENTORY IS IMPLEMENTED]"
        // add the sword to the builder.inventory
        game.printer.waiting("\"${builder.name}, you may leave now; best of luck in your quests.\"").then {
            game.printer += "You exit the castle with your new broadsword sheathed on your back."
            resolve(builder)
        }
    }

    /**
     * The final meeting with the king as an archer.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun archerHumanText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"Alright.\""
        game.printer += "\"To aid you on your quests, I will grant you a simple, yet effective longbow.\""
        game.printer.waiting("You walk closer to the throne and he bestows upon you the longbow. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add the bow to the builder.inventory
                game.printer += "\"I will also give you a small dagger because I feel like it.\""
                game.printer += "\"I'm the king, after all.\""
                game.printer.waiting("The king hands you a dagger.").then {
                    // add the dagger to the builder.inventory
                    game.printer += "\"Oh yes, you probably would like some arrows as well, I suppose?\""
                    game.printer.waiting("He hands you a quiver full of arrows. [TODO WHEN INVENTORY IS IMPLEMENTED]")
                        // add the arrows to the builder.inventory
                        .then {
                            game.printer.waiting("\"${builder.name}, you may leave now. Good luck on your quest.")
                                .then {
                                    game.printer += "You exit the castle with your new broadsword sheathed on your back."
                                    resolve(builder)
                                }
                        }
                }
            }
    }

    /**
     * The final meeting with the king as a wizard.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun wizardHumanText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"We could use a new wizard. I'm glad you chose to become one.\""
        game.printer.waiting("\"We'll give you the standard oak staff to begin with, it should get the job done for now. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add the oak staff to builder.inventory
                // todo figure out mana as well
                game.printer.waiting("\"${builder.name}, you may leave now. Good luck on your quest.")
                    .then {
                        game.printer += "You exit the castle carrying your new oak staff."
                        resolve(builder)
                    }
            }

    }

    /**
     * Returns the human job name for whatever class they are.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @throws IllegalStateException if the job is still somehow null
     * @param builder the builder object
     * @return a string of the job name
     */
    private fun getHumanJob(builder: Player.Builder): String {
        return when (builder.pclass) {
            Player.Class.MELEE -> "knight"
            Player.Class.RANGED -> "archer"
            Player.Class.MAGIC -> "wizard"
            null -> throw IllegalStateException()
        }
    }


    /**
     * The meeting about the class for the elf.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun elfMeeting(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "You make your way into your mentor's house."
        game.printer += "Your mentor is upstairs at their desk where they normally are."
        game.printer += "They gracefully turn to face you."
        game.printer += "\"Greetings, ${builder.name}.\""
        Menu.basic(
            game,
            "\"So, you've decided to become a ${getElfJob(builder)}. Are you sure of that choice?\"",
            "Yes",
            "No"
        ).then { option ->
            when (option) {
                0 -> {
                    when (builder.pclass) {
                        Player.Class.MELEE -> duelistElfText(builder).then { resolve(it) }
                        Player.Class.RANGED -> archerElfText(builder).then { resolve(it) }
                        Player.Class.MAGIC -> mageElfText(builder).then { resolve(it) }
                    }
                }
                1 -> {
                    recheckClassElf(builder)
                }
            }
        }
    }

    /**
     * Rechecks the class if the user is unsure of what class they want to be.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     */
    private fun recheckClassElf(builder: Player.Builder) {
        Menu.basic(game, "\"What do you wish to be, then?\"", "Duelist", "Archer", "Mage").then { option ->
            when (option) {
                0 -> {
                    builder.pclass = Player.Class.MELEE
                    duelistElfText(builder)
                }
                1 -> {
                    builder.pclass = Player.Class.RANGED
                    archerElfText(builder)
                }
                2 -> {
                    builder.pclass = Player.Class.MAGIC
                    mageElfText(builder)
                }
            }
        }
    }

    /**
     * The final meeting with the mentor as a duelist.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun duelistElfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"I agree you as a duelist would be best.\""
        game.printer += "\"Your rapier skills are already unparalleled among those your age.\""
        game.printer += "\"As is customary, you shall take my rapier for your quest.\""
        game.printer.waiting("They deliver their oldest, yet still perfectly maintained, rapier into your waiting hands. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add rapier to builder.inventory
                game.printer += "\"Good luck on whatever quest you choose, young one.\""
                game.printer.waiting("\"To help you out, I shall give you 750 gold coins. Spend them wisely.\" [TODO WHEN INVENTORY IS IMPLEMENTED]")
                    .then {
                        // Add coins to builder.inventory
                        game.printer += "You step outside the house with your rapier in a scabbard on your hip."
                        resolve(builder)
                    }
            }
    }

    /**
     * The final meeting with the mentor as an archer.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun archerElfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"I agree; archery would be best for you.\""
        game.printer += "\"Your marksmanship is unparalleled among those your age.\""
        game.printer += "\"As is customary, you shall take my bow for your quest.\""
        game.printer.waiting("Your mentor hands you their oldest, yet still perfectly maintained, longbow into your waiting hands. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add longbow to builder.inventory
                game.printer.waiting("They hand you a quiver filled with arrows to go along with it. [TODO WHEN INVENTORY IS IMPLEMENTED]")
                    .then {
                        // add arrows to builder.inventory
                        game.printer += "\"Take this dagger as well.\""
                        game.printer.waiting("Your mentor gives you a small, sharp dagger. [TODO WHEN INVENTORY IS IMPLEMENTED]")
                            .then {
                                // add dagger to builder.inventory
                                game.printer += "\"Good luck on whatever quest you choose, young one.\""
                                game.printer.waiting("\"To help you out, I shall give you 750 gold coins. Spend them wisely.\" [TODO WHEN INVENTORY IS IMPLEMENTED]")
                                    .then {
                                        // add coins to builder.inventory
                                        game.printer += "You step outside the house with your longbow slung over your shoulder."
                                        resolve(builder)
                                    }
                            }
                    }
            }

    }

    /**
     * The final meeting with the mentor as a mage.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun mageElfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"I agree that you would be best as a mage.\""
        game.printer += "\"Your aptitude for spells is unparalleled among those your age.\""
        game.printer += "\"As is customary, you shall take my staff for your quest.\""
        game.printer.waiting("Your mentor hands you their oldest, yet perfectly maintained oak staff into your hands [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add the staff to builder.inventory
                game.printer += "\"Best of luck on wherever your fate leads you, young one.\""
                game.printer.waiting("Before you go, however, I shall give you 750 gold coins to help you out. Spend them well.\" [TODO WHEN INVENTORY IS IMPLEMENTED]")
                    .then {
                        // add coins to builder.inventory
                        game.printer += "You step outside the house with your staff in hand."
                        resolve(builder)
                    }
            }
    }

    /**
     * Returns the elf job name for whatever class they are.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @throws IllegalStateException if the job is still somehow null
     * @param builder the builder object
     * @return a string of the job name
     */
    private fun getElfJob(builder: Player.Builder): String {
        return when (builder.pclass) {
            Player.Class.MELEE -> "duelist"
            Player.Class.RANGED -> "archer"
            Player.Class.MAGIC -> "mage"
            null -> throw IllegalStateException()
        }
    }

    /**
     * The meeting about the class for the dwarf.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun dwarfMeeting(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "You confidently stroll into the Great Hall."
        game.printer += "The king is drinking ale at a table and gestures to sit down next to him."
        game.printer.waiting("\"Oi, ${builder.name}! I'm proud of yah.\"").then {
            Menu.basic(
                game,
                "\"My right-hand man right here tells me that yah wanna become a ${getDwarfJob(builder)}. Yah sure about that?\"",
                "Yep",
                "Nah"
            ).then { option ->
                when (option) {
                    0 -> {
                        when (builder.pclass) {
                            Player.Class.MELEE -> fighterDwarfText(builder).then { resolve(it) }
                            Player.Class.RANGED -> slingerDwarfText(builder).then { resolve(it) }
                            Player.Class.MAGIC -> sorcererDwarfText(builder).then { resolve(it) }
                        }
                    }
                    1 -> {
                        recheckClassDwarf(builder)
                    }
                }
            }
        }
    }

    /**
     * Rechecks the class if the user is unsure of what class they want to be.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     */
    private fun recheckClassDwarf(builder: Player.Builder) {
        Menu.basic(game, "\"Whatcha gonna be then?\"", "Fighter", "Slinger", "Sorcerer").then { option ->
            when (option) {
                0 -> {
                    builder.pclass = Player.Class.MELEE
                    fighterDwarfText(builder)
                }
                1 -> {
                    builder.pclass = Player.Class.RANGED
                    slingerDwarfText(builder)
                }
                2 -> {
                    builder.pclass = Player.Class.MAGIC
                    sorcererDwarfText(builder)
                }
            }
        }
    }

    /**
     * The final meeting with the king as a fighter.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun fighterDwarfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"You'll be a good 'un.\""
        game.printer += "\"Take this axe.\""
        game.printer.waiting("The king hands you a battleaxe. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add axe to builder.inventory
                game.printer += "\"I want some peace and quiet now. Do whichever quest you're doing or don't come back at all.\""
//                game.printer.delay(3000)
                game.printer.waiting("\"Nah, I'm just kiddin'. Good luck!\"")
                    .then {
                        game.printer += "You step out of the Great Hall with your battleaxe on your back."
                        resolve(builder)
                    }
            }
    }

    /**
     * The final meeting with the mentor as a slinger.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun slingerDwarfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"A slinger? I didn't peg yah as a coward!\""
//        game.printer.delay(3000)
        game.printer += "\"I'm just joshin' with yah! A slinger would be a good job for yah.\""
        game.printer += "\"I'll give you a nice sling, don'tcha worry.\""
        game.printer.waiting("The king hands you a sling and a carton of rounded balls of lead. [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add sling and lead balls to builder.inventory
                game.printer += "You lift it. \"Damn, that's heavy,\" you think to yourself."
                game.printer += "\"Take this dagger as well.\""
                game.printer.waiting("The king hands you a small, sharp dagger. [TODO WHEN INVENTORY IS IMPLEMENTED]")
                    .then {
                        // add small dagger to builder.inventory
                        game.printer += "\"I want some peace and quiet now. Do that quest or don't come back at all.\""
//                        game.printer.delay(3000)
                        game.printer.waiting("\"Nah, I'm just kiddin'. Good luck on your quest!\"").then {
                            game.printer += "You step out of the Great Hall with your sling and lead balls in your pocket."
                            resolve(builder)
                        }
                    }
            }
    }

    /**
     * The final meeting with the mentor as a sorcerer.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @param builder the builder instance
     * @return a promise of the player builder
     */
    private fun sorcererDwarfText(builder: Player.Builder) = Promise<Player.Builder> { resolve ->
        game.printer.clear()
        game.printer += "\"A sorcerer, eh? You better be useful to me!\""
        game.printer += "\"Take this cool stick I found on the ground on my daily walk yesterday.\""
        game.printer += "\"They always say that it's not the quality of the stick, it's how you use it!\""
        game.printer.waiting("The king hands you the cool stick. Thanks a lot... [TODO WHEN INVENTORY IS IMPLEMENTED]")
            .then {
                // add the cool stick to builder.inventory
                game.printer += "\"I want some peace and quiet now. Do that quest or don't come back at all.\""
//            game.printer.delay(3000)
                game.printer.waiting("\"Nah, I'm just kiddin'. Good luck on your quest!\"")
                    .then {
                        game.printer += "You step out of the Great Hall with your sling and lead balls in your pocket."
                        resolve(builder)
                    }
            }
    }

    /**
     * Returns the dwarf job name for whatever class they are.
     *
     * @author Jonathan Metcalf
     * @since 1.3
     *
     * @throws IllegalStateException if the job is still somehow null
     * @param builder the builder object
     * @return a string of the job name
     */
    private fun getDwarfJob(builder: Player.Builder): String {
        return when (builder.pclass) {
            Player.Class.MELEE -> "fighter"
            Player.Class.RANGED -> "slinger"
            Player.Class.MAGIC -> "sorcerer"
            null -> throw IllegalStateException()
        }
    }
}