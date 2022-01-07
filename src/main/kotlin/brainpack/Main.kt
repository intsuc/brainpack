package brainpack

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.required
import java.io.File
import java.util.zip.ZipOutputStream

fun main(args: Array<String>) {
    val parser = ArgParser("brainpack")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file").required()

    parser.parse(args)

    operator fun <A, B, C> ((A) -> B).rangeTo(that: (B) -> C): (A) -> C = { that(this(it)) }

    ZipOutputStream(File(output).outputStream().buffered()).use { out ->
        val source = File(input).readText()
        (::parse..::fuse..::structure..::generate..::write)(source)(out)
    }
}
