package brainpack.phase

import brainpack.Structured

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
    val inc = "inc"
    val set = "set"
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
            "data remove storage $namespace $reversed[-1]",
            "function $namespace${print}_${Byte.MIN_VALUE}_${Byte.MAX_VALUE}",
            "execute if data storage $namespace $reversed[0] run function $namespace$print"
        )
    )

    fun tree(min: Int, max: Int) {
        functions["${print}_${min}_$max"] = if (max - min > 4) (0..3).map {
            val size = (max - min + 1) / 4
            val min1 = min + it * size
            val max1 = min1 + size - 1
            tree(min1, max1)
            "execute if score $data $objective matches $min1..$max1 run function $namespace${print}_${min1}_$max1"
        } else (min..max).map {
            val value = when (val char = it.toUByte().toInt().toChar()) {
                '\n' -> """'"\\n"'"""
                '\r' -> """'"\\r"'"""
                '"' -> """'"\\""'"""
                '\'' -> """'"\'"'"""
                '\\' -> """'"\\\\"'"""
                else -> """'"$char"'"""
            }
            "execute if score $data $objective matches ${it.toByte()} run data modify storage $namespace $printed append value $value"
        }
    }

    tree(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt())

    var loopId = 0

    fun visit(current: MutableList<String>, instructions: List<Structured>): Unit = instructions.forEach {
        when (it) {
            is Structured.IncPtr -> current += "function $namespace$incPtr"
            is Structured.DecPtr -> current += "function $namespace$decPtr"
            is Structured.Inc -> {
                val path = "${inc}_${it.value}"
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
            is Structured.Set -> {
                val path = "${set}_${it.value}"
                functions.computeIfAbsent(path) { _ ->
                    listOf(
                        "data modify storage $namespace $memoryRight[-1] set value ${it.value}b"
                    )
                }
                current += "function $namespace$path"
            }
            is Structured.Write -> current += "function $namespace$write"
            is Structured.Read -> current += "function $namespace$read"
            is Structured.Loop -> {
                val path = "loop_${loopId++}"
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
        """tellraw @s {"nbt": "$printed[]", "separator": "", "interpret": true, "storage": "$namespace"}"""
    )

    return functions
}
