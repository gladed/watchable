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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

/** Bind [dest] so that it receives values from [origin] as long as this [CoroutineScope] lives. */
fun <T, V, M : T, C : Change<T, V>> CoroutineScope.bind(
    dest: MutableWatchable<T, V, M, C>,
    origin: Watchable<T, V, C>
) = dest.bind(this, origin)

/**
 * Deliver changes for this [Watchable] to [func], starting with its initial state, until
 * the returned [WatchHandle] is closed or this [CoroutineScope] completes.
 */
fun <T, V, C : Change<T, V>> CoroutineScope.watchSimple(
    watchable: Watchable<T, V, C>,
    func: suspend SimpleChange<V>.() -> Unit
) = watchable.watchSimple(this@watchSimple, func)

/**
 * Deliver changes for this [Watchable] to [func], starting with its initial state, until
 * the returned [WatchHandle] is closed or this [CoroutineScope] completes.
 */
fun <T, V, C : Change<T, V>> CoroutineScope.watch(
    watchable: Watchable<T, V, C>,
    func: suspend (C) -> Unit
) = watchable.watch(this@watch, func)

/**
 * Deliver multiple changes for this [Watchable] to [func], starting with its initial state, until
 * the returned [WatchHandle] is closed or this [CoroutineScope] completes.
 */
fun <T, V, C : Change<T, V>> CoroutineScope.batch(
    watchable: Watchable<T, V, C>,
    /** The minimum time between change notifications in milliseconds, or 0 for no delay. */
    minPeriod: Long = 0,
    func: suspend (List<C>) -> Unit
) = watchable.batch(this@batch, minPeriod, func)

/**
 * Bind [dest] so that it receives changes from [origin] and applies them with [apply] for as long as
 * this [CoroutineScope] lives.
 */
fun <T, V, M : T, C : Change<T, V>, T2, V2, C2 : Change<T2, V2>> CoroutineScope.bind(
    dest: MutableWatchable<T, V, M, C>,
    origin: Watchable<T2, V2, C2>,
    apply: M.(C2) -> Unit
) = dest.bind(this, origin, apply)

/**
 * Perform some work in the background until the returned handle is closed or cancelled.
 */
internal fun CoroutineScope.operate(
    /** Work to perform, stopping as soon as the supplied Mutex can be locked. */
    work: suspend CoroutineScope.(Mutex) -> Unit
): WatchHandle {

    // May be unlocked to allow [work] to shut down gently.
    val mutex = Mutex(locked = true)

    // Uses supervisor to allow the parent scope to complete without waiting
    val operationScope = CoroutineScope(coroutineContext + SupervisorJob())
    val operation = operationScope.launch {
        work(mutex)
    }

    // When the current scope completes, cancel the operation
    coroutineContext[Job]?.invokeOnCompletion { operation.cancel() }

    return object : WatchHandle {
        override fun cancel() { operation.cancel() }
        override suspend fun join() { operation.join() }
        override fun close() {
            if (mutex.isLocked) mutex.unlock()
        }
    }
}
