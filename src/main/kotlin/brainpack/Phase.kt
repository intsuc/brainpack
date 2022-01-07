package brainpack

fun parse(source: String): List<Command> = mutableListOf<Command>().also { list ->
    var depth = 0

    source.forEach {
        when (it) {
            '>' -> list += Command.INC_PTR
            '<' -> list += Command.DEC_PTR
            '+' -> list += Command.INC
            '-' -> list += Command.DEC
            '.' -> list += Command.WRITE
            ',' -> list += Command.READ
            '[' -> {
                list += Command.BEGIN
                ++depth
            }
            ']' -> {
                list += Command.END
                --depth
            }
            else -> {}
        }
    }

    if (depth != 0) throw Exception("[] mismatched")
}

fun fuse(commands: List<Command>): List<Fused> = mutableListOf<Fused>().also { stack ->
    commands.forEach {
        when (it) {
            Command.INC_PTR -> when (val last = stack.lastOrNull()) {
                is Fused.IncPtr -> {
                    stack.removeLast()
                    if (last.value + 1 != 0) stack += last.copy(last.value + 1)
                }
                else -> stack += Fused.IncPtr(1)
            }
            Command.DEC_PTR -> when (val last = stack.lastOrNull()) {
                is Fused.IncPtr -> {
                    stack.removeLast()
                    if (last.value - 1 != 0) stack += last.copy(last.value - 1)
                }
                else -> stack += Fused.IncPtr(-1)
            }
            Command.INC -> when (val last = stack.lastOrNull()) {
                is Fused.Inc -> {
                    stack.removeLast()
                    if (last.value + 1 != 0) stack += last.copy(last.value + 1)
                }
                else -> stack += Fused.Inc(1)
            }
            Command.DEC -> when (val last = stack.lastOrNull()) {
                is Fused.Inc -> {
                    stack.removeLast()
                    if (last.value - 1 != 0) stack += last.copy(last.value - 1)
                }
                else -> stack += Fused.Inc(-1)
            }
            Command.WRITE -> stack += Fused.Write
            Command.READ -> stack += Fused.Read
            Command.BEGIN -> stack += Fused.Begin
            Command.END -> stack += Fused.End
        }
    }
}
