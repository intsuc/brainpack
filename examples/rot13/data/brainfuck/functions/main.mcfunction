scoreboard objectives remove brainfuck
scoreboard objectives add brainfuck dummy
data modify storage brainfuck: memory_left set value []
data modify storage brainfuck: memory_right set value [0b]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: memory_right append from storage brainfuck: memory_right[]
data modify storage brainfuck: output set value []
function brainfuck:set_-1
function brainfuck:read
function brainfuck:inc_1
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_0
function brainfuck:reverse
data remove storage brainfuck: printed
function brainfuck:print
tellraw @s {"nbt": "printed[]", "separator": "", "interpret": true, "storage": "brainfuck:"}
