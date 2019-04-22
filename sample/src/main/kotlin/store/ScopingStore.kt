package store

import io.gladed.watchable.util.Stoppable
import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * A [Store] factory producing stores that trigger operations on its items while in use.
 *
 * For any new object retrieved, [start] is called. This operation is stopped only when the item
 * is deleted, or all scopes using it have completed.
 */
class ScopingStore<T : Any>(
    /** The parent context for starting and stopping operations. */
    context: CoroutineContext,
    /** The store being wrapped. */
    val back: Store<T>,
    private val start: suspend T.() -> Stoppable
) : CoroutineScope {
    override val coroutineContext = context + Job()

    private val map = mutableMapOf<String, Holding>().guarded()

    /**
     * Return a new [Store]; items accessed by this store will have a corresponding operation (see [start]) in effect
     * until the completion of all scopes using the item.
     */
    fun create(scope: CoroutineScope): Store<T> {
        val newStore = SingleStore()
        scope.coroutineContext[Job]?.invokeOnCompletion {
            launch {
                map {
                    // Yank this store from all holds
                    val dead = filterValues { it.remove(newStore) }
                    // Yank all newly stopped holds
                    keys.removeAll(dead.keys)
                }
            }
        }
        return newStore
    }

    /** Release everything regardless of the state of scopes. */
    suspend fun stop() {
        map { toMap().also { clear() } }.values
            .forEach { it.stop() }
    }

    /** Attempt to hold an instance of [T] on behalf of one or more [Store]s. */
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

        /** Stop holding. */
        suspend fun stop() {
            // If the request isn't done then cancel it
            watching.cancel()
            @Suppress("EmptyCatchBlock") // Ignore cancellations
            try {
                // Stop watching if not already cancelled
                if (!watching.isCancelled) {
                    watching.await().second.stop()
                }
            } catch (c: CancellationException) { }
        }
    }

    /** A [Store] whose objects are held when accessing them. */
    private inner class SingleStore : Store<T> {
        override suspend fun put(key: String, value: T) {
            // Put a hold on the value
            hold(key) { value }.apply {
                if (first !== value) {
                    cannot("replace while different object with same key is in use")
                }
            }
            // Put it in the backing store
            back.put(key, value)
        }

        @Suppress("TooGenericExceptionCaught") // Rollback in case of any failure
        override suspend fun get(key: String): T = try {
            hold(key) { back.get(key) }.first
        } catch (t: Throwable) {
            // On any failure remove this item from the global map.
            map { remove(key) }
            throw t
        }

        override suspend fun delete(key: String) {
            map { get(key) }?.stop()
            back.delete(key)
        }

        private suspend fun hold(key: String, getValueFunc: suspend () -> T): Pair<T, Stoppable> =
            map {
                get(key)?.also { it.add(this@SingleStore) }
                    ?: Holding(this@SingleStore, async(SupervisorJob()) {
                        val value = getValueFunc()
                        value to value.start()
                    }).also {
                        put(key, it)
                    }
            }.watching.await()
    }
}
