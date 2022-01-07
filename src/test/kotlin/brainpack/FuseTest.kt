package brainpack

import brainpack.Command.*
import kotlin.test.Test
import kotlin.test.assertEquals

class FuseTest {
    @Test
    fun fuseInc() {
        assertEquals(
            listOf(Fused.Inc(2)),
            fuse(listOf(INC, INC))
        )
    }

    @Test
    fun fuseDec() {
        assertEquals(
            listOf(Fused.Inc(-2)),
            fuse(listOf(DEC, DEC))
        )
    }

    @Test
    fun fuseIncDec() {
        assertEquals(
            emptyList(),
            fuse(listOf(INC, DEC))
        )
    }

    @Test
    fun fuseDecInc() {
        assertEquals(
            emptyList(),
            fuse(listOf(DEC, INC))
        )
    }

    @Test
    fun fuseIncPtr() {
        assertEquals(
            listOf(Fused.IncPtr(2)),
            fuse(listOf(INC_PTR, INC_PTR))
        )
    }

    @Test
    fun fuseDecPtr() {
        assertEquals(
            listOf(Fused.IncPtr(-2)),
            fuse(listOf(DEC_PTR, DEC_PTR))
        )
    }

    @Test
    fun fuseIncPtrDecPtr() {
        assertEquals(
            emptyList(),
            fuse(listOf(INC_PTR, DEC_PTR))
        )
    }

    @Test
    fun fuseDecPtrIncPtr() {
        assertEquals(
            emptyList(),
            fuse(listOf(DEC_PTR, INC_PTR))
        )
    }
}
