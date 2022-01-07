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
    fun add() {
        assertEquals(
            listOf(BEGIN, DEC, INC_PTR, INC, DEC_PTR, END),
            parse("[->+<]")
        )
    }
}
