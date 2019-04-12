package io.gladed.watchable

import io.gladed.watchable.Period.IMMEDIATE
import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.util.Guard
import io.gladed.watchable.watcher.ImmediateWatcher
import io.gladed.watchable.watcher.InlineWatcher
import io.gladed.watchable.watcher.PeriodicWatcher
import io.gladed.watchable.watcher.Watcher
import kotlinx.coroutines.CoroutineScope
import java.lang.ref.WeakReference

/** Base for an object that can be watched for changes. */
abstract class WatchableBase<C : Change> : Watchable<C> {

    /** Objects watching this one. */
    private val watchers = Guard(mutableListOf<WeakReference<Watcher<C>>>())

    /** Deliver changes to watchers if any. */
    protected suspend fun dispatch(change: List<C>) {
        // Upon any change, dispatch it to all watchers
        watchers {
            val toRemove = mapNotNull { it.get() }
                .filter { !it.dispatch(change) }
            removeAll { ref ->
                ref.get()?.let { toRemove.contains(it) } ?: true
            }
        }
    }

    /** Return the initial change that a new watcher should receive, if any */
    protected abstract fun getInitialChange(): C?

    override suspend fun batch(
        scope: CoroutineScope,
        period: Long,
        func: suspend (List<C>) -> Unit
    ): Busy =
        when {
            period == INLINE -> InlineWatcher(scope.coroutineContext, func)
            period == IMMEDIATE -> ImmediateWatcher(scope.coroutineContext, func)
            period < 0 -> throw IllegalArgumentException("Invalid period")
            else -> PeriodicWatcher(scope.coroutineContext, period, func)
        }.also { watcher ->
            watchers {
                add(WeakReference(watcher))
                sortBy {
                    when (it) {
                        is InlineWatcher<*> -> 1 // Inlines always come first
                        else -> 2
                    }
                }

                getInitialChange()?.also { change ->
                    watcher.dispatch(listOf(change))
                }
            }
        }
}
