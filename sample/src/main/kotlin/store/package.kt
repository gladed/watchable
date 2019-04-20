package store

import io.gladed.watchable.Watcher
import kotlin.coroutines.CoroutineContext

/**
 * Return a [ScopingStore] around this [Store], so that objects within it can be watched as long as necessary from
 * other CoroutineScopes.
 */
fun <T : Any> Store<T>.scope(context: CoroutineContext, watchFunc: suspend T.() -> Watcher) =
    ScopingStore(context, this, watchFunc)

/** Throw an exception to complain that something cannot be done. */
fun cannot(doSomething: String): Nothing = throw Cannot("Cannot $doSomething")
