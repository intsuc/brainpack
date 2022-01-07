# brainpack

A brainfuck to datapack compiler.

## Usage

### Compile

<pre><code>brainpack -i <i>input_file</i> -o <i>output_file</i></code></pre>

### Run

```mcfunction
data modify storage brainfuck: input ...
function brainfuck:main
data get storage brainfuck: output
```
