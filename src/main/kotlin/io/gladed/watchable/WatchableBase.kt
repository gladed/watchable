/*
 * (c) Copyright 2019 Glade Diviney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gladed.watchable

import io.gladed.watchable.Period.IMMEDIATE
import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.util.Guard
import io.gladed.watchable.watcher.Immediate
import io.gladed.watchable.watcher.Inline
import io.gladed.watchable.watcher.Periodic
import io.gladed.watchable.watcher.WatcherBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/** Base for an object that generates change events of type [C] as its underlying data changes. */
abstract class WatchableBase<C : Change> : Watchable<C> {

    class Droppable<T : Any>(value: T) {
        private val ref = WeakReference(value)
        private var holding: T? = value
        fun drop() { holding = null }
        fun get(): T? { return ref.get() }
    }

    /** Objects watching this one. */
    private val watchers = Guard(mutableListOf<Droppable<WatcherBase<C>>>())

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

    override fun batch(
        scope: CoroutineScope,
        period: Long,
        func: suspend (List<C>) -> Unit
    ): Watcher {
        val changeWatcher = when {
            period == INLINE -> Inline(scope.coroutineContext, func)
            period == IMMEDIATE -> Immediate(scope.coroutineContext, func)
            period < 0 -> throw IllegalArgumentException("Invalid period")
            else -> Periodic(scope.coroutineContext, period, func)
        }

        // This function is not suspending (to prevent users from having to launch, which
        // creates a scope that immediately dies). So we must do setup in background.
        val setupWatcher = scope.launch {
            val toDrop = Droppable(changeWatcher)
            watchers {
                add(toDrop)
                sortBy {
                    when (it) {
                        is Inline<*> -> 1 // INLINE always comes first
                        else -> 2
                    }
                }

                getInitialChange()?.also { change ->
                    changeWatcher.dispatch(listOf(change))
                }
            }

            scope.coroutineContext[Job]?.invokeOnCompletion {
                toDrop.drop()
            }
        }.toWatcher()

        return setupWatcher + changeWatcher
    }

    private fun Job.toWatcher() = object : Watcher {
        override suspend fun start() { join() }
        override fun cancel() { this@toWatcher.cancel() }
        override suspend fun stop() { join() }
    }
}
