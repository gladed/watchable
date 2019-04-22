package store

/** Convert between "deflated" ([A]) and "inflated" ([B]) forms of an object. */
interface Inflater<A : Any, B : Any> {
    /** Convert an instance of [A] to [B]. */
    fun inflate(value: A): B

    /** Convert an instance of [B] to [A]. */
    fun deflate(value: B): A

    /** Combine this [Inflater] with [other] to produce a transitive [Inflater]. */
    operator fun <C : Any> plus(other: Inflater<B, C>): Inflater<A, C> = object : Inflater<A, C> {
        override fun inflate(value: A): C =
            other.inflate(this@Inflater.inflate(value))

        override fun deflate(value: C): A =
            this@Inflater.deflate(other.deflate(value))
    }
}
