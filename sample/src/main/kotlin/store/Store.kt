package store

import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import util.cannot
import kotlin.coroutines.CoroutineContext

/** Something that stores elements. */
interface Store<T : Any> {
    /** Obtain the item matching the key, throwing if this cannot be done for any reason. */
    suspend fun get(key: String): T
}

class MemoryStore<T : Any> : Store<T>{
    val map = mutableMapOf<String, T>().guarded()

    override suspend fun get(key: String): T =
        map { get(key) ?: cannot("get item by key") }

    suspend fun put(key: String, value: T): T? =
        map { put(key, value) }
}

/** An item that is held and must be explicitly released when all users are finished with it. */
interface Hold<T : Any> {
    /** The value being held. */
    val value: T

    /** MUST be called to release all access to the value. */
    suspend fun release()
}

/** Callbacks which must be implemented by any HoldingStore. */
interface HoldingStoreCallbacks<T, H> {
    suspend fun onHold(value: T): H
    suspend fun onRelease(value: T, holder: H)
}

abstract class HoldingStore<T : Any, H>(
    private val back: Store<T>
) : Store<Hold<T>>, HoldingStoreCallbacks<T, H> {
    override suspend fun get(key: String): Hold<T> {
        val value = back.get(key)
        val holder = onHold(value)
        return object : Hold<T> {
            override val value = value

            override suspend fun release() {
                onRelease(value, holder)
            }
        }
    }
}

class ScopedStore<T : Any>(
    context: CoroutineContext,
    private val back: Store<Hold<T>>
) : CoroutineScope {
    override val coroutineContext = context + Job()
    private inner class Holding(val holder: Deferred<Hold<T>>) {
        val stores = mutableSetOf<Store<T>>().guarded()
    }

    private val map = mutableMapOf<String, Holding>().guarded()

    fun create(scope: CoroutineScope): Store<T> {
        val newStore = object : Store<T> {
            override suspend fun get(key: String): T {
                val self = this
                // TODO: Test piggybacking (two gets from two scopes at same time)
                val holding = map {
                    this[key] ?: Holding(async { back.get(key) }).also {
                        this[key] = it
                    }
                }
                holding.stores { add(self) }
                return holding.holder.await().value
            }
        }

        scope.coroutineContext[Job]?.invokeOnCompletion {
            this@ScopedStore.launch {
                releaseAll(newStore)
            }
        }
        return newStore
    }

    private suspend fun releaseAll(store: Store<T>) {
        map {
            val toEject = entries.filter {
                // Waiting for all requests to finish is probably bad.
                val holding = it.value
                holding.stores {
                    remove(store)
                    isEmpty().also { empty ->
                        if (empty) {
                            // If the request isn't done then cancel it
                            holding.holder.cancel()
                            try {
                                // Wait for the holder to arrive (if possible) then release it
                                holding.holder.await().release()
                            } catch (c: CancellationException) { }
                        }
                    }
                }
            }.map { it.key }
            keys.removeAll(toEject)
        }
    }
}
