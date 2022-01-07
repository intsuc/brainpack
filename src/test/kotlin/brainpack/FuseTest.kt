package brainpack

import brainpack.Instruction.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FuseTest {
    @Test
    fun inc() {
        assertEquals(
            listOf(Fused.Inc(2)),
            fuse(listOf(INC, INC))
        )
    }

    @Test
    fun dec() {
        assertEquals(
            listOf(Fused.Inc(-2)),
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
}
