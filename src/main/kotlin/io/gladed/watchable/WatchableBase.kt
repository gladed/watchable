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
import io.gladed.watchable.util.guarded
import io.gladed.watchable.watcher.Immediate
import io.gladed.watchable.watcher.Inline
import io.gladed.watchable.watcher.Periodic
import io.gladed.watchable.watcher.WatcherBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async

/** Base for an object that generates change events of type [C] as its underlying data changes. */
abstract class WatchableBase<C : Change> : Watchable<C> {

    /** Objects watching this one. */
    private val watchers = mutableListOf<WatcherBase<C>>().guarded()

    /** Deliver changes to watchers if any. */
    protected suspend fun dispatch(change: List<C>) {
        // Upon any change, dispatch it to all watchers, removing dead ones
        watchers {
            val toRemove = filter { !it.dispatch(change) }
            removeAll(toRemove)
        }
    }

    /** Return the initial change that a new watcher should receive, if any */
    protected abstract fun getInitialChange(): C?

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override fun batch(
        scope: CoroutineScope,
        period: Long,
        func: suspend (List<C>) -> Unit
    ): Watcher {
        // Asynchronously prepare a watcher
        val setup = scope.async {
            when {
                period == INLINE -> Inline(scope.coroutineContext, func)
                period == IMMEDIATE -> Immediate(scope.coroutineContext, func)
                period < 0 -> throw IllegalArgumentException("Invalid period")
                else -> Periodic(scope.coroutineContext, period, func)
            }.also { watcher ->
                watchers {
                    // Send the initial change
                    getInitialChange()?.also { change ->
                        watcher.dispatch(listOf(change))
                    }

                    // Start handling other dispatches
                    add(watcher)

                    // Sort INLINE to top
                    sortBy { if (it is Inline<*>) 1 else 2 }
                }
            }
        }

        // Hide the asynchronous behavior behind a front-end Watcher
        return object : Watcher {
            override fun cancel() {
                if (setup.isCancelled) return
                setup.cancel()
                if (setup.isCompleted && setup.getCompletionExceptionOrNull() == null) {
                    setup.getCompleted().cancel()
                }
            }

            override suspend fun stop() {
                if (setup.isCancelled) return
                setup.await().also { watcher ->
                    watchers { remove(watcher) }
                    watcher.stop()
                }
            }

            override suspend fun start() {
                if (setup.isCancelled) return
                setup.await().start()
            }
        }
    }
}
