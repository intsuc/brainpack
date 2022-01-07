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
function brainfuck:inc_8
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_0
function brainfuck:inc_ptr
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_1
function brainfuck:inc_1
function brainfuck:dec_ptr
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_2
function brainfuck:inc_ptr
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_13
function brainfuck:dec_ptr
function brainfuck:inc_11
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_16
function brainfuck:inc_ptr
function brainfuck:inc_-1
function brainfuck:write
function brainfuck:inc_ptr
function brainfuck:inc_-1
function brainfuck:write
function brainfuck:inc_7
function brainfuck:write
function brainfuck:inc_11
function brainfuck:write
function brainfuck:dec_ptr
function brainfuck:write
function brainfuck:inc_ptr
function brainfuck:inc_ptr
function brainfuck:write
function brainfuck:inc_2
function brainfuck:write
function brainfuck:inc_7
function brainfuck:write
function brainfuck:write
function brainfuck:dec_ptr
function brainfuck:inc_-1
function brainfuck:write
function brainfuck:inc_ptr
function brainfuck:inc_ptr
function brainfuck:inc_-1
function brainfuck:load
execute unless score data brainfuck matches 0 run function brainfuck:loop_17
function brainfuck:reverse
data remove storage brainfuck: printed
function brainfuck:print
tellraw @s {"nbt": "printed[]", "separator": "", "interpret": true, "storage": "brainfuck:"}
