package util

/** An object that retrieves elements by key. */
interface Store<T : Any> {
    /** Return the corresponding element, or throw if not present. */
    suspend fun get(key: String): T

    /**
     * Write something to the store at the given key, overwriting what was there, if anything.
     */
    suspend fun put(key: String, value: T)
}
