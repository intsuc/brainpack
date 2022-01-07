package brainpack

enum class Command {
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
    data class IncPtr(val value: Int) : Fused()
    data class Inc(val value: Int) : Fused()
    object Write : Fused()
    object Read : Fused()
    object Begin : Fused()
    object End : Fused()
}
