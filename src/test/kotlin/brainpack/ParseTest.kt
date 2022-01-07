package brainpack

import brainpack.Instruction.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class ParseTest {
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
    fun all() {
        assertEquals(
            listOf(INC_PTR, DEC_PTR, INC, DEC, WRITE, READ, BEGIN, END),
            parse("><+-.,[]")
        )
    }
}
