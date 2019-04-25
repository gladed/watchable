package store

import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

/** A RAM cache that prevents overuse of backing store by serving objects it already has loaded. */
class Cache<T : Any>(
    context: CoroutineContext,
    private val back: Store<T>
) : Store<T>, CoroutineScope {
    override val coroutineContext = context + Job()

    private val map = mutableMapOf<String, WeakReference<T>>().guarded()

    override suspend fun get(key: String): T =
        map {
            clearDead()
            get(key)
        }?.get() ?: back.get(key).also {
            map { put(key, WeakReference(it)) }
        }

    override suspend fun put(key: String, value: T) {
        map {
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
        map { remove(key) }
        back.delete(key)
    }

    override fun keys() = back.keys()
}
