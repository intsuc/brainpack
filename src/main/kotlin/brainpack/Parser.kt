package brainpack

fun parse(source: String): List<Command> = mutableListOf<Command>().also { commands ->
    var depth = 0

    source.forEach {
        when (it) {
            '>' -> commands += Command.INC_PTR
            '<' -> commands += Command.DEC_PTR
            '+' -> commands += Command.INC
            '-' -> commands += Command.DEC
            '.' -> commands += Command.WRITE
            ',' -> commands += Command.READ
            '[' -> {
                commands += Command.BEGIN
                ++depth
            }
            ']' -> {
                commands += Command.END
                --depth
            }
            else -> {}
        }
    }

    if (depth != 0) throw Exception("[] mismatched")
}
