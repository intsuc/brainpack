package brainpack.phase

import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

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
