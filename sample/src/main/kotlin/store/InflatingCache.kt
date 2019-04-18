package store

import io.gladed.watchable.util.guarded
import kotlinx.coroutines.CoroutineScope
import model.Deflateable
import model.Identified
import model.Inflater

abstract class InflatingCache<T: Identified, U: Deflateable<T>>(
    private val inflater: Inflater<T, U>,
    private val getter: suspend (String) -> T?
) {
    private val map = mutableMapOf<String, U>().guarded()

    abstract fun onInflate(value: U)

    suspend fun get(scope: CoroutineScope, id: String): U? =
        map { this[id] } ?: getter(id)?.let { flat ->
            // TODO: Address race because getter might already be in progress, should attach to async
            with(inflater) {
                flat.inflate().also {
                    map { this[id] = it }
                    // TODO: count scope references eh
                }
            }
        }


}
