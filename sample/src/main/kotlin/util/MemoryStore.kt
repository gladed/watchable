package util

import io.gladed.watchable.util.guarded

/** A store entirely in RAM. */
class MemoryStore<T : Any> : Store<T> {
    val map = mutableMapOf<String, T>().guarded()

    override suspend fun get(key: String): T =
        map { get(key) ?: cannot("get item by key") }

    override suspend fun put(key: String, value: T) {
        map { put(key, value) }
    }
}
