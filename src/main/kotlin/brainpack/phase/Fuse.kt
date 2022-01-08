package brainpack.phase

import brainpack.Fused
import brainpack.Instruction


fun fuse(instructions: List<Instruction>): List<Fused> = mutableListOf<Fused>().also { stack ->
    fun set(value: Byte) {
        when (stack.lastOrNull()) {
            is Fused.Inc -> stack.removeLast()
            is Fused.Set -> stack.removeLast()
            else -> {}
        }
        stack += Fused.Set(value)
    }

    set(0)

    instructions.forEach {
        when (it) {
            Instruction.INC_PTR -> when (stack.lastOrNull()) {
                is Fused.DecPtr -> stack.removeLast()
                else -> stack += Fused.IncPtr
            }
            Instruction.DEC_PTR -> when (stack.lastOrNull()) {
                is Fused.IncPtr -> stack.removeLast()
                else -> stack += Fused.DecPtr
            }
            Instruction.INC -> when (val last = stack.lastOrNull()) {
                is Fused.Inc -> {
                    stack.removeLast()
                    if (last.value.inc() != 0) stack += last.copy(last.value.inc())
                }
                is Fused.Set -> {
                    stack.removeLast()
                    set(last.value.inc())
                }
                else -> stack += Fused.Inc(1)
            }
            Instruction.DEC -> when (val last = stack.lastOrNull()) {
                is Fused.Inc -> {
                    stack.removeLast()
                    if (last.value.dec() != 0) stack += last.copy(last.value.dec())
                }
                is Fused.Set -> {
                    stack.removeLast()
                    set(last.value.dec())
                }
                else -> stack += Fused.Inc(-1)
            }
            Instruction.WRITE -> stack += Fused.Write
            Instruction.READ -> stack += Fused.Read
            Instruction.BEGIN -> stack += Fused.Begin
            Instruction.END -> when (stack.lastOrNull()) {
                is Fused.Inc -> when (stack.dropLast(1).lastOrNull()) {
                    is Fused.Begin -> {
                        stack.removeLast()
                        stack.removeLast()
                        set(0)
                    }
                    else -> stack += Fused.End
                }
                else -> stack += Fused.End
            }
        }
    }
}
