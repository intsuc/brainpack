package brainpack

import brainpack.Fused.Inc
import brainpack.Fused.Set
import brainpack.Instruction.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FuseTest {
    @Test
    fun inc() {
        assertEquals(
            listOf(Inc(2)),
            fuse(listOf(INC, INC))
        )
    }

    @Test
    fun dec() {
        assertEquals(
            listOf(Inc(-2)),
            fuse(listOf(DEC, DEC))
        )
    }

    @Test
    fun incDec() {
        assertEquals(
            emptyList(),
            fuse(listOf(INC, DEC))
        )
    }

    @Test
    fun decInc() {
        assertEquals(
            emptyList(),
            fuse(listOf(DEC, INC))
        )
    }

    @Test
    fun incPtrDecPtr() {
        assertEquals(
            emptyList(),
            fuse(listOf(INC_PTR, DEC_PTR))
        )
    }

    @Test
    fun decPtrIncPtr() {
        assertEquals(
            emptyList(),
            fuse(listOf(DEC_PTR, INC_PTR))
        )
    }

    @Test
    fun beginIncEnd() {
        assertEquals(
            listOf(Set(0)),
            fuse(listOf(BEGIN, INC, END))
        )
    }

    @Test
    fun beginDecEnd() {
        assertEquals(
            listOf(Set(0)),
            fuse(listOf(BEGIN, DEC, END))
        )
    }

    @Test
    fun beginIncIncEnd() {
        assertEquals(
            listOf(Set(0)),
            fuse(listOf(BEGIN, INC, INC, END))
        )
    }

    @Test
    fun setInc() {
        assertEquals(
            listOf(Set(1)),
            fuse(listOf(BEGIN, DEC, END, INC))
        )
    }

    @Test
    fun setIncInc() {
        assertEquals(
            listOf(Set(2)),
            fuse(listOf(BEGIN, DEC, END, INC, INC))
        )
    }
}
