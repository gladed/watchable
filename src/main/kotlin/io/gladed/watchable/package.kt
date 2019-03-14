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
import kotlinx.coroutines.channels.ReceiveChannel

/** Return a new [WatchableValue] wrapping [value], living on this [CoroutineScope]. */
fun <T : Any> watchableValueOf(value: T) = WatchableValue(value)

/** Return a new [WatchableSet] containing the elements of this [Collection]. */
fun <T : Any> Collection<T>.toWatchableSet() = WatchableSet(this)

/** Return a new [WatchableSet] containing [values], living on this [CoroutineScope]. */
fun <T : Any> watchableSetOf(vararg values: T) = values.toSet().toWatchableSet()

/** Return a new [WatchableList] containing the elements of this [Collection]. */
fun <T : Any> Collection<T>.toWatchableList() = WatchableList(this)

/** Return a new [WatchableList] containing [values], living on this [CoroutineScope]. */
fun <T : Any> watchableListOf(vararg values: T) = values.toList().toWatchableList()

/** Return a new [WatchableMap] containing the elements of this [Map]. */
fun <K, V> Map<K, V>.toWatchableMap() = WatchableMap(this)

/** Return a new [WatchableMap] containing a map of [values], living on this [CoroutineScope]. */
fun <K, V> watchableMapOf(vararg values: Pair<K, V>) = values.toMap().toWatchableMap()

/** Bind [dest] so that it receives values from [origin] as long as this [CoroutineScope] lives. */
fun <T, M : T, C : Change<T>> CoroutineScope.bind(dest: MutableWatchable<T, M, C>, origin: Watchable<T, C>) =
    dest.bind(this, origin)

/**
 * Deliver changes for this [Watchable] to [func], starting with its initial state, until
 * the returned job is cancelled or this [CoroutineScope] completes.
 */
fun <T, C : Change<T>> CoroutineScope.watch(
    watchable: Watchable<T, C>,
    func: (C) -> Unit
) = watchable.watch(this@watch, func)

/**
 * Deliver multiple changes for this [Watchable] to [func], starting with its initial state, until
 * the returned job is cancelled or this [CoroutineScope] completes.
 */
fun <T, C : Change<T>> CoroutineScope.batch(
    watchable: Watchable<T, C>,
    /** The minimum time between change notifications in milliseconds, or 0 for no delay. */
    minPeriod: Long = 0,
    func: suspend (List<C>) -> Unit
) = watchable.batch(this@batch, minPeriod, func)

/**
 * Bind [dest] so that it receives changes from [origin] and applies them with [apply] for as long as
 * this [CoroutineScope] lives.
 */
fun <T, M : T, C : Change<T>, T2, C2 : Change<T2>> CoroutineScope.bind(
    dest: MutableWatchable<T, M, C>,
    origin: Watchable<T2, C2>,
    apply: M.(C2) -> Unit
) = dest.bind(this, origin, apply)

/**
 * Create a [ReceiveChannel] for intercepting lists of changes made to [target] for as long as this
 * [CoroutineScope] lives.
 */
fun <T, C : Change<T>> CoroutineScope.subscribe(target: Watchable<T, C>): ReceiveChannel<List<C>> =
    target.subscribe(this)

/**
 * Create and return a group of watchable objects that itself is watchable.
 */
fun group(vararg watchables: Watchable<out Any, out Change<Any>>) = WatchableGroup(watchables.toList())
