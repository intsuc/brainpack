package brainpack

import brainpack.phase.structure
import kotlin.test.Test
import kotlin.test.assertEquals
import brainpack.Fused as F
import brainpack.Structured as S

class StructureTest {
    @Test
    fun noLoop() {
        assertEquals(
            listOf(S.Write),
            structure(listOf(F.Write))
        )
    }

    @Test
    fun emptyLoop() {
        assertEquals(
            listOf(S.Loop(emptyList())),
            structure(listOf(F.Begin, F.End))
        )
    }

    @Test
    fun singleLoop() {
        assertEquals(
            listOf(S.Loop(listOf(S.Write))),
            structure(listOf(F.Begin, F.Write, F.End))
        )
    }

    @Test
    fun multipleLoops() {
        assertEquals(
            listOf(S.Loop(listOf(S.Write)), S.Loop(listOf(S.Read))),
            structure(listOf(F.Begin, F.Write, F.End, F.Begin, F.Read, F.End))
        )
    }

    @Test
    fun nestedLoops() {
        assertEquals(
            listOf(S.Loop(listOf(S.Loop(listOf(S.Write))))),
            structure(listOf(F.Begin, F.Begin, F.Write, F.End, F.End))
        )
    }
}
