package store

import io.gladed.watchable.util.Stoppable
import kotlin.coroutines.CoroutineContext

/**
 * Return a [ScopingStore] around this [Store].
 */
fun <T : Any> Store<T>.scope(context: CoroutineContext, start: suspend T.() -> Stoppable) =
    ScopingStore(context, this, start)

/** Throw an exception to complain that something cannot be done. */
fun cannot(doSomething: String): Nothing = throw Cannot(doSomething)
