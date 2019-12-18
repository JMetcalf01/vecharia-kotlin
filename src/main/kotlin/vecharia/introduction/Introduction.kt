package vecharia.introduction

import vecharia.Input
import vecharia.Vecharia
import vecharia.entity.Entity
import vecharia.entity.Player
import vecharia.menu.Menu
import vecharia.render.Text
import vecharia.util.Promise
import java.util.*

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

    fun introduction() {
        Promise<Player.Builder> { builder ->
            builder(Player.Builder())
        }.then { resolve: (Player.Builder) -> Unit, builder ->
            game.printer.clear()
            game.printer += "You wake from a deep sleep. Rubbing your eyes blearily, you roll over and sit up."
            game.printer += "What's your name again?"
            var readName: (String) -> Unit = { }
            readName = { str ->
                if (str.length in 2..16) {
                    builder.name = str
                    resolve(builder)
                } else {
                    game.printer += "Name must be between 2 and 16 characters long."
                    Input.readInput().then(readName)
                }
            }
            Input.readInput().then(readName)
        }.then { builder ->
            game.printer.clear()
            game.printer.waiting("Right. It's ${builder.name}.").then {
                game.printer.clear()

                Menu.basic(game, "What race are you?", "Human", "Elf", "Dwarf").then {
                    game.printer.clear()
                    when (it) {
                        0 -> {
                            builder.race = Entity.Race.HUMAN
                            game.printer += "That's right! You are a young human in the town of Zloridge."
                            game.printer += "Zloridge is under the rule of King Henry IV."
                            game.printer += "The day to become a full-fledged adventurer has come!"
                            game.printer += "Your pay is meager, but you have managed to save up 1000 gold coins for your quest. [TODO WHEN INVENTORY IS IMPLEMENTED]"
                            game.printer += "If you keep forgetting basic stuff like that you're a human, maybe you should be sleeping more..."
                            game.printer += Text("You dismiss that thought from your mind and get changed, find something to eat, and leave your hut.", wait = true)
                        }
                        1 -> {
                            builder.race = Entity.Race.ELF
                            game.printer += "You are a young elf in the elven town of Mythmerius."
                            game.printer += "You answer to no king; Mythmerius is a democracy."
                            game.printer += "Today is the day that you choose the quest you wish to attempt."
                            game.printer += "If you keep forgetting basic stuff like the fact that you're an elf, maybe you should be sleeping more..."
                            game.printer += Text("You dismiss that thought from your mind and get changed, find something to eat, and leave your house.", wait = true)
                        }
                        2 -> {
                            builder.race = Entity.Race.DWARF
                            game.printer += "You are a young dwarf in the dwarven town of Mil Gurum."
                            game.printer += "Your leader is the proud King Meldal."
                            game.printer += "Today you will finally choose your quest!"
                            game.printer += "Your pay is not much, but you have managed to save up 1250 gold coins for your quest. [TODO WHEN INVENTORY IS IMPLEMENTED]"
                            game.printer += "How could you forget you're a dwarf?"
                            game.printer += Text("You climb out of bed, struggling to reach the floor. Why are these beds so damn high?", wait = true)
                        }
                    }
                }
            }
        }
    }

    private fun getFriendName(race: Entity.Race): String {
        return when (race) {
            Entity.Race.HUMAN -> HUMAN_NAMES[rand.nextInt(HUMAN_NAMES.size)]
            Entity.Race.ELF -> ELF_NAMES[rand.nextInt(ELF_NAMES.size)]
            Entity.Race.DWARF -> DWARF_NAMES[rand.nextInt(DWARF_NAMES.size)]
        }
    }

}