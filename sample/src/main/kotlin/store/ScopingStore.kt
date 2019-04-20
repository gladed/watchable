package store

import io.gladed.watchable.Stoppable
import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/** A [Store] factory producing stores that hold items on behalf of other scopes. */
class ScopingStore<T : Any>(
    context: CoroutineContext,
    val back: Store<T>,
    private val start: suspend T.() -> Stoppable
) : CoroutineScope {
    override val coroutineContext = context + Job()

    /** Watch what is going on for one or more users of a [T] object. */
    private inner class Holding(first: Store<T>, val watching: Deferred<Pair<T, Stoppable>>) {
        private val stores = mutableSetOf(first).guarded()

        suspend fun add(store: Store<T>) {
            stores { add(store) }
        }

        /** Removes a store and returns true if this object can be discarded (e.g. no more stores). */
        suspend fun remove(store: Store<T>) =
            if (stores { remove(store); isEmpty() }) {
                stop()
                true
            } else {
                false
            }

        /** Stop the hold wherever it is. */
        suspend fun stop() {
            // If the request isn't done then cancel it
            watching.cancel()
            @Suppress("EmptyCatchBlock") // Ignore cancellations
            try {
                // Wait for the holder to arrive (if possible) then release it
                watching.await().second.stop()
            } catch (c: CancellationException) { }
        }
    }

    private val map = mutableMapOf<String, Holding>().guarded()

    private inner class SingleStore : Store<T> {
        override suspend fun put(key: String, value: T) {
            // Push the value back
            back.put(key, value)
            hold(key) { value }.apply {
                if (first !== value) {
                    cannot("replace while different object with same key is in use")
                }
            }
        }

        @Suppress("TooGenericExceptionCaught") // Rollback in case of any failure
        override suspend fun get(key: String): T = try {
            hold(key) { back.get(key) }.first
        } catch (t: Throwable) {
            // On any failure remove this item from the global map.
            map { remove(key) }
            throw t
        }

        private suspend fun hold(key: String, getValueFunc: suspend () -> T): Pair<T, Stoppable> =
            map {
                get(key)?.also { it.add(this@SingleStore) }
                    ?: Holding(this@SingleStore, async(SupervisorJob()) {
                        val value = getValueFunc()
                        value to value.start()
                    }).also { put(key, it) }
            }.watching.await()
    }

    /**
     * Return a new [Store] for which all items put or retrieved from this store are held for at least
     * as long as [scope] survives.
     */
    fun create(scope: CoroutineScope): Store<T> {
        val newStore = SingleStore()
        scope.coroutineContext[Job]?.invokeOnCompletion {
            launch {
                releaseAll(newStore)
            }
        }
        return newStore
    }

    private suspend fun releaseAll(store: Store<T>) {
        map {
            keys.removeAll(filterValues { it.remove(store) }.keys)
        }
    }

    /** Release everything regardless of the state of scopes. */
    suspend fun stop() {
        map { toMap().also { clear() } }.values
            .forEach { it.stop() }
    }
}
