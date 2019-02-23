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

package com.gladed.watchable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun <T : Any> CoroutineScope.watchableValueOf(value: T) = WatchableValue(coroutineContext, value)

fun <T : Any> Collection<T>.toWatchableSet(context: CoroutineContext) = WatchableSet(context, this)

fun <T : Any> Collection<T>.toWatchableSet(scope: CoroutineScope) = toWatchableSet(scope.coroutineContext)

fun <T : Any> CoroutineScope.watchableSetOf(vararg values: T) = values.toList().toWatchableSet(this)

fun <T : Any> Collection<T>.toWatchableList(context: CoroutineContext) = WatchableList(context, this)

fun <T : Any> Collection<T>.toWatchableList(scope: CoroutineScope) = toWatchableList(scope.coroutineContext)

fun <T : Any> CoroutineScope.watchableListOf(vararg values: T) = values.toList().toWatchableList(this)

fun <K, V> Map<K, V>.toWatchableMap(context: CoroutineContext) = WatchableMap(context, this)

fun <K, V> Map<K, V>.toWatchableMap(scope: CoroutineScope) = toWatchableMap(scope.coroutineContext)

fun <K, V> CoroutineScope.watchableMapOf(vararg values: Pair<K, V>) = values.toMap().toWatchableMap(this)

/**
 * Return a [Job] that for the duration of this [CoroutineScope], invokes [handler] for any changes to [watchable]
 * (including its initial state.)
 */
fun <T, C : Change<T>> CoroutineScope.watch(watchable: Watchable<T, C>, handler: (C) -> Unit): Job =
    with(watchable) {
        this@watch.watch(handler)
    }

/**
 * For this scope, cancel the channel when the scope is closed. This means that any `runBlocking` scope
 * must be cancelled to escape, because this coroutine will never complete a join().
 */
internal fun <T> CoroutineScope.cancelWithScope(channel: SendChannel<T>) {
    launch {
        delay(Long.MAX_VALUE)
    }.invokeOnCompletion {
        channel.close()
    }
}
