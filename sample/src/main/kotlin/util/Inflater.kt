package util

interface Inflater<A : Any, B : Any> {
    /** Convert this instance of [A] to [B]. */
    fun A.inflate(): B
    /** Convert this instance of [B] to [A]. */
    fun B.deflate(): A
}
