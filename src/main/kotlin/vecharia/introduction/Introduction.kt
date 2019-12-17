package vecharia.introduction

import vecharia.Input
import vecharia.Vecharia
import vecharia.entity.Player
import vecharia.util.Promise

class Introduction(val game: Vecharia) {

    fun introduction() {
        Promise<Player.Builder> { builder ->
            builder(Player.Builder())
        }.then { resolve: (Player.Builder) -> Unit, builder ->
            game.printer.clear()
            game.printer += "You wake from a deep sleep. Rubbing your eyes blearily, you roll over and sit up."
            game.printer += "What's your name again?"
            Input.readInput().then { str ->
                builder.name = str
                resolve(builder)
            }
        }.then { resolve: (Player.Builder) -> Unit, builder ->
            game.printer.clear()
            game.printer += "Right. It's ${builder.name}."
        }

    }
}