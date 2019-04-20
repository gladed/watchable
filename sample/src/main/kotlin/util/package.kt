package util

import io.gladed.watchable.Watcher
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

/** Convert this [Store] of deflated items [D] into a [Store] of inflated items [I]. */
fun <D : Any, I : Any> Store<D>.inflate(inflater: Inflater<D, I>): Store<I> =
    object : Store<I> {
        override suspend fun get(key: String): I =
            with(inflater) { this@inflate.get(key).inflate() }

        override suspend fun put(key: String, value: I) {
            with(inflater) { this@inflate.put(key, value.deflate()) }
        }
    }

/**
 * Return a [ScopingStore] around this [Store], so that objects within it can be watched as long as necessary from
 * other CoroutineScopes.
 */
fun <T : Any> Store<T>.scope(context: CoroutineContext, watchFunc: suspend T.() -> Watcher) =
    ScopingStore(context, this, watchFunc)

/** Convert this [Store] of String objects in JSON format to a [Store] of [T] objects. */
fun <T : Any> Store<String>.json(serializer: KSerializer<T>) =
    object : Store<T> {
        override suspend fun get(key: String): T =
            Json.parse(serializer, this@json.get(key))

        override suspend fun put(key: String, value: T) {
            this@json.put(key, Json.stringify(serializer, value))
        }
    }

/** Throw an exception to complain that something cannot be done. */
fun cannot(doSomething: String): Nothing = throw Cannot("Cannot $doSomething")
