package brainpack

enum class Instruction {
    INC_PTR,
    DEC_PTR,
    INC,
    DEC,
    WRITE,
    READ,
    BEGIN,
    END
}

sealed class Fused {
    object IncPtr : Fused()
    object DecPtr : Fused()
    data class Inc(val value: Int) : Fused()
    data class Set(val value: Byte) : Fused()
    object Write : Fused()
    object Read : Fused()
    object Begin : Fused()
    object End : Fused()
}

sealed class Structured {
    object IncPtr : Structured()
    object DecPtr : Structured()
    data class Inc(val value: Int) : Structured()
    data class Set(val value: Byte) : Structured()
    object Write : Structured()
    object Read : Structured()
    data class Loop(val body: List<Structured>) : Structured()
}
