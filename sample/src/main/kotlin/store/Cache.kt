package store

import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

/** A RAM cache that prevents overuse of backing store by serving objects it already has loaded. */
class Cache<T : Any>(
    context: CoroutineContext,
    private val back: Store<T>
) : Store<T>, CoroutineScope {
    override val coroutineContext = context + Job()

    private val finding = mutableMapOf<String, Deferred<T>>().guarded()
    private val found = mutableMapOf<String, WeakReference<T>>().guarded()

    override suspend fun get(key: String): T =
        found {
            clearDead()
            get(key)?.get()
        } ?: finding { startGetting(key) }.await()

    private fun MutableMap<String, Deferred<T>>.startGetting(key: String): Deferred<T> =
        getOrPut(key) {
            async {
                val result = back.get(key)
                if (quashFind(key)) {
                    // Only record the result if we quashed the find
                    found { put(key, WeakReference(result)) }
                }
                result
            }
        }

    override suspend fun put(key: String, value: T) {
        quashFind(key) // Outstanding gets should not update cache
        found {
            clearDead()
            put(key, WeakReference(value))
        }
        back.put(key, value)
    }

    /** Eliminate unused refs. */
    private fun MutableMap<String, WeakReference<T>>.clearDead() {
        entries.removeIf { it.value.get() == null }
    }

    override suspend fun delete(key: String) {
        quashFind(key) // Outstanding gets should not update cache
        found { remove(key) }
        back.delete(key)
    }

    private suspend fun quashFind(key: String): Boolean =
        finding { remove(key) } != null

    override fun keys() = back.keys()
}
