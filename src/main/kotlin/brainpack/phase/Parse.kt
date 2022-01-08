package brainpack.phase

import brainpack.Instruction

fun parse(source: String): List<Instruction> = mutableListOf<Instruction>().also { instructions ->
    var depth = 0

    source.forEach {
        when (it) {
            '>' -> instructions += Instruction.INC_PTR
            '<' -> instructions += Instruction.DEC_PTR
            '+' -> instructions += Instruction.INC
            '-' -> instructions += Instruction.DEC
            '.' -> instructions += Instruction.WRITE
            ',' -> instructions += Instruction.READ
            '[' -> {
                instructions += Instruction.BEGIN
                ++depth
            }
            ']' -> {
                instructions += Instruction.END
                --depth
            }
            else -> {}
        }
    }

    if (depth != 0) throw Exception("[] mismatched")
}
