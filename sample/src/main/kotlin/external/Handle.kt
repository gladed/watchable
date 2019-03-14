package external

import kotlinx.coroutines.CompletionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext

/**
 * A scope that completes when all other contexts are removed or completed.
 */
class Handle<T>(
    /** The initial context for this [Handle]. */
    context: CoroutineContext,
    val value: T
) : CoroutineScope {
    override val coroutineContext = context + Job()
    private val contexts = mutableListOf<CoroutineContext>()

    init {
        this += context
    }

    fun invokeOnCompletion(handler: CompletionHandler): DisposableHandle =
        coroutineContext[Job]!!.invokeOnCompletion(handler)

    override fun toString() = "Handle($contexts, $value)"

    /** Add a context to this [Handle]. */
    operator fun plusAssign(context: CoroutineContext) {
        if (!isActive) throw IllegalStateException("Cannot add context to inactive scope")
        contexts += context
        context[Job]?.invokeOnCompletion {
            this -= context
        }
    }

    /** Remove a context from consideration by this [Handle]. */
    operator fun minusAssign(context: CoroutineContext) {
        contexts -= context
        if (contexts.isEmpty()) {
            coroutineContext[Job]?.cancel()
        }
    }

}
