package brainpack

import brainpack.Command.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ParserTest {
    @Test
    fun missingBegin() {
        assertFails {
            parse("]")
        }
    }

    @Test
    fun missingEnd() {
        assertFails {
            parse("[")
        }
    }

    @Test
    fun add() {
        assertEquals(
            listOf(BEGIN, DEC, INC_PTR, INC, DEC_PTR, END),
            parse("[->+<]")
        )
    }
}
