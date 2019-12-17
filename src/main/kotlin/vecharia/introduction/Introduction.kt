package vecharia.introduction

import vecharia.Input
import vecharia.Vecharia
import vecharia.entity.Player
import vecharia.render.Text
import vecharia.util.Promise

class Introduction(val game: Vecharia) {

    fun introduction() {
        val player: Promise<Player.Builder> = Promise { builder ->
            builder(Player.Builder())
        }
        println("HEREEEE")

        // Name
        player.then { resolve: (Player.Builder) -> Unit, builder ->
            game.printer.clear()
            game.printer += "You wake from a deep sleep. Rubbing your eyes blearily, you roll over and sit up."
            game.printer += "What's your name again?"
            game.printer += Text(">", newLine = false, instant = true)
            Input.readInput().then { str ->
                print("here: $str")
                builder.name = str
                resolve(builder)
            }
        }.then { builder ->
            println(builder.name)
        }

    }
}