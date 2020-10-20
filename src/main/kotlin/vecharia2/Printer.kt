package vecharia2

object Printer {

    // OUTPUT

    suspend fun write(vararg text: String) = write(*text.map(::Text).toTypedArray())
    suspend fun write(vararg text: Text) {}

    suspend fun prompt(text: String) = prompt(Text(text))
    suspend fun prompt(text: Text) {}

    // INPUT

    //todo Text variant
    suspend fun getInput(prompt: String): String = "Not Implemented"

    //todo Text variant
    suspend fun getSelection(prompt: String, vararg options: String): String = "Not Implemented"
}

data class Text(val text: String)