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
import kotlinx.coroutines.CoroutineScope

/** Bind [dest] so that it receives values from [origin] as long as the calling coroutineContext lives. */
fun <M, C : Change> CoroutineScope.bind(
    dest: MutableWatchable<M, C>,
    origin: Watchable<C>
) = dest.bind(this, origin)

/**
 * Deliver simplified changes for this [Watchable] as receiver objects to [func] until
 * the returned [Watcher] is closed or this [CoroutineScope] completes.
 */
fun <S, C : HasSimpleChange<S>> CoroutineScope.simple(
    watchable: SimpleWatchable<S, C>,
    func: suspend S.() -> Unit
) = watchable.simple(this@simple, func)

/**
 * Deliver changes for this [Watchable] to [func] until the returned [Watcher] is closed or this
 * [CoroutineScope] completes.
 */
fun <C : Change> CoroutineScope.watch(
    watchable: Watchable<C>,
    /** When to receive changes, see [Period]. */
    period: Long = IMMEDIATE,
    func: suspend (C) -> Unit
) = watchable.watch(this@watch, period, func)

/**
 * Deliver multiple changes for this [Watchable] to [func] until the returned [Watcher] is closed or this
 * [CoroutineScope] completes.
 */
fun <C : Change> CoroutineScope.batch(
    watchable: Watchable<C>,
    /** When to receive changes, see [Period]. */
    period: Long = IMMEDIATE,
    func: suspend (List<C>) -> Unit
) = watchable.batch(this@batch, period, func)

/**
 * Bind [dest] so that it receives changes from [origin] and applies them with [apply] for as long as
 * this [CoroutineScope] lives.
 */
fun <M, C : Change, C2 : Change> CoroutineScope.bind(
    dest: MutableWatchable<M, C>,
    origin: Watchable<C2>,
    /** When to receive changes, see [Period]. */
    period: Long = IMMEDIATE,
    apply: M.(C2) -> Unit
) = dest.bind(this, origin, period, apply)

/** Suspend until [condition] returns true, calling it after each group of changes. */
suspend fun <C : Change, W : Watchable<C>> CoroutineScope.waitFor(
    target: W,
    condition: (W) -> Boolean
) {
    target.waitFor(this) { condition(target) }
}
