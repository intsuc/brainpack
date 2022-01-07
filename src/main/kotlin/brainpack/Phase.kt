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

fun structure(commands: List<Fused>): List<Structured> =
    mutableListOf(mutableListOf<Structured>())
        .also { stacks ->
            commands.forEach {
                when (it) {
                    is Fused.IncPtr -> stacks.last() += Structured.IncPtr(it.value)
                    is Fused.Inc -> stacks.last() += Structured.Inc(it.value)
                    is Fused.Write -> stacks.last() += Structured.Write
                    is Fused.Read -> stacks.last() += Structured.Read
                    is Fused.Begin -> stacks.add(mutableListOf())
                    is Fused.End -> {
                        val body = stacks.removeLast()
                        stacks.last() += Structured.Loop(body)
                    }
                }
            }
        }
        .flatten()
