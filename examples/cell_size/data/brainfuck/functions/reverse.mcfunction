data modify storage brainfuck: reversed append from storage brainfuck: output[-1]
data remove storage brainfuck: output[-1]
execute if data storage brainfuck: output[0] run function brainfuck:reverse
