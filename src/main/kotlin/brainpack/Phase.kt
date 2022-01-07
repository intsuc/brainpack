package brainpack

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

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

fun fuse(instructions: List<Instruction>): List<Fused> = mutableListOf<Fused>().also { stack ->
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
                    if (last.value + 1 != 0) stack += last.copy(last.value + 1)
                }
                else -> stack += Fused.Inc(1)
            }
            Instruction.DEC -> when (val last = stack.lastOrNull()) {
                is Fused.Inc -> {
                    stack.removeLast()
                    if (last.value - 1 != 0) stack += last.copy(last.value - 1)
                }
                else -> stack += Fused.Inc(-1)
            }
            Instruction.WRITE -> stack += Fused.Write
            Instruction.READ -> stack += Fused.Read
            Instruction.BEGIN -> stack += Fused.Begin
            Instruction.END -> stack += Fused.End
        }
    }
}

fun structure(instructions: List<Fused>): List<Structured> =
    mutableListOf(mutableListOf<Structured>())
        .also { stacks ->
            instructions.forEach {
                when (it) {
                    is Fused.IncPtr -> stacks.last() += Structured.IncPtr
                    is Fused.DecPtr -> stacks.last() += Structured.DecPtr
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

fun generate(instructions: List<Structured>): Map<String, List<String>> {
    val objective = "brainfuck"
    val data = "data"

    val namespace = "brainfuck:"

    val memoryLeft = "memory_left"
    val memoryRight = "memory_right"
    val output = "output"
    val input = "input"
    val reversed = "reversed"
    val printed = "printed"

    val main = "main"
    val incPtr = "inc_ptr"
    val decPtr = "dec_ptr"
    val write = "write"
    val read = "read"
    val load = "load"
    val reverse = "reverse"
    val print = "print"

    val mainBody = mutableListOf(
        "scoreboard objectives remove $objective",
        "scoreboard objectives add $objective dummy",
        "data modify storage $namespace $memoryLeft set value []",
        "data modify storage $namespace $memoryRight set value [0b]",
    ).also {
        it += List(15) { "data modify storage $namespace $memoryRight append from storage $namespace $memoryRight[]" }
        it += "data modify storage $namespace output set value []"
    }

    val functions = mutableMapOf(
        main to mainBody,
        incPtr to listOf(
            "data modify storage $namespace $memoryLeft append from storage $namespace $memoryRight[-1]",
            "data remove storage $namespace $memoryRight[-1]"
        ),
        decPtr to listOf(
            "data modify storage $namespace $memoryRight append from storage $namespace $memoryLeft[-1]",
            "data remove storage $namespace $memoryLeft[-1]"
        ),
        write to listOf(
            "data modify storage $namespace $output append from storage $namespace $memoryRight[-1]"
        ),
        read to listOf(
            "data modify storage $namespace $memoryRight[-1] set from storage $namespace $input[-1]",
            "data remove storage $namespace $input[-1]"
        ),
        load to listOf(
            "execute store result score $data $objective run data get storage $namespace $memoryRight[-1] 1.0"
        ),
        reverse to listOf(
            "data modify storage $namespace $reversed append from storage $namespace $output[-1]",
            "data remove storage $namespace $output[-1]",
            "execute if data storage $namespace $output[0] run function $namespace$reverse"
        ),
        print to listOf(
            "execute store result score $data $objective run data get storage $namespace $reversed[-1] 1.0",
            "data remove storage $namespace $reversed[-1]"
        ) + (0..255).map { // TODO: use 4-ary tree
            val value = when (val char = it.toChar()) {
                '\n' -> """"\\n""""
                '\r' -> """"\\r""""
                '"' -> """'"'"""
                '\'' -> """"'""""
                '\\' -> """"\\""""
                else -> """"$char""""
            }
            "execute if score $data $objective matches ${it.toByte()} run data modify storage $namespace $printed append value $value"
        } + "execute if data storage $namespace $reversed[0] run function $namespace$print",
    )

    var id = 0

    fun visit(current: MutableList<String>, instructions: List<Structured>): Unit = instructions.forEach {
        when (it) {
            is Structured.IncPtr -> current += "function $namespace$incPtr"
            is Structured.DecPtr -> current += "function $namespace$decPtr"
            is Structured.Inc -> {
                val path = "inc_${it.value}"
                functions.computeIfAbsent(path) { _ ->
                    listOf(
                        "function $namespace$load"
                    ) + if (it.value >= 0) {
                        "execute store result storage $namespace $memoryRight[-1] byte 1.0 run scoreboard players add $data $objective ${it.value}"
                    } else {
                        "execute store result storage $namespace $memoryRight[-1] byte 1.0 run scoreboard players remove $data $objective ${-it.value}"
                    }
                }
                current += "function $namespace$path"
            }
            is Structured.Write -> current += "function $namespace$write"
            is Structured.Read -> current += "function $namespace$read"
            is Structured.Loop -> {
                val path = "loop_${id++}"
                val body = mutableListOf<String>()
                functions[path] = body
                current += listOf(
                    "function $namespace$load",
                    "execute unless score $data $objective matches 0 run function $namespace$path"
                )
                visit(body, it.body)
                body += listOf(
                    "function $namespace$load",
                    "execute unless score $data $objective matches 0 run function $namespace$path"
                )
            }
        }
    }

    visit(mainBody, instructions)

    mainBody += listOf(
        "function $namespace$reverse",
        "data remove storage $namespace $printed",
        "function $namespace$print",
        """tellraw @s {"nbt": "$printed", "storage": "$namespace", "interpret": true}"""
    )

    return functions
}

fun write(functions: Map<String, List<String>>): (ZipOutputStream) -> Unit = { out ->
    out.putNextEntry(ZipEntry("pack.mcmeta"))
    out.write(
        """{
        |  "pack": {
        |    "description": "",
        |    "pack_format": 8
        |  }
        |}
        |""".trimMargin().toByteArray()
    )
    out.closeEntry()

    functions.forEach { (path, commands) ->
        out.putNextEntry(ZipEntry("data/brainfuck/functions/$path.mcfunction"))
        commands.forEach {
            out.write(it.toByteArray())
            out.write('\n'.code)
        }
        out.closeEntry()
    }
}
