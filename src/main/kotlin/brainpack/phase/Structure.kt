package brainpack.phase

import brainpack.Fused
import brainpack.Structured

fun structure(instructions: List<Fused>): List<Structured> =
    mutableListOf(mutableListOf<Structured>())
        .also { stacks ->
            instructions.forEach {
                when (it) {
                    is Fused.IncPtr -> stacks.last() += Structured.IncPtr
                    is Fused.DecPtr -> stacks.last() += Structured.DecPtr
                    is Fused.Inc -> stacks.last() += Structured.Inc(it.value)
                    is Fused.Set -> stacks.last() += Structured.Set(it.value)
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
