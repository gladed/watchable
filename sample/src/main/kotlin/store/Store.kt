package store

/** An object that retrieves elements by key. */
interface Store<T : Any> {
    /** Return the corresponding element, or throw if not present. */
    suspend fun get(key: String): T

    /** Write something to the store at the given key, overwriting what was there, if anything. */
    suspend fun put(key: String, value: T)

    /** Delete any data found at [key]. */
    suspend fun delete(key: String)

    /** Convert this [Store] of deflated items into a [Store] of inflated items [U]. */
    fun <U : Any> inflate(inflater: Inflater<T, U>): Store<U> = object : Store<U> {
        override suspend fun get(key: String): U =
            inflater.inflate(this@Store.get(key))

        override suspend fun put(key: String, value: U) {
            this@Store.put(key, inflater.deflate(value))
        }

        override suspend fun delete(key: String) {
            this@Store.delete(key)
        }
    }
}
