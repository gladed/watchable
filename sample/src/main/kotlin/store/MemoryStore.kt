package store

import io.gladed.watchable.util.guarded

/** A store entirely in RAM. */
class MemoryStore<T : Any>(private val name: String) : Store<T> {

    val map = mutableMapOf<String, T>().guarded()

    override suspend fun get(key: String): T =
        map { get(key) ?: cannot("get $name by key") }

    override suspend fun put(key: String, value: T) {
        map { put(key, value) }
    }

    override suspend fun delete(key: String) {
        map { remove(key) }
    }

    /** Return the keys available in memory at this moment. */
    suspend fun keys() = map { keys.toSet() }
}
