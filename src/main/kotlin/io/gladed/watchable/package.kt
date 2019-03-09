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
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlin.coroutines.CoroutineContext

/** Return a new [WatchableValue] wrapping [value], living on this [CoroutineScope]. */
fun <T : Any> CoroutineScope.watchableValueOf(value: T) = WatchableValue(coroutineContext, value)

/** Return a new [WatchableSet] containing the elements of this [Collection], watchable on the supplied [context]. */
fun <T : Any> Collection<T>.toWatchableSet(context: CoroutineContext) = WatchableSet(context, this)

/** Return a new [WatchableSet] containing the elements of this [Collection], living  on the supplied [scope]. */
fun <T : Any> Collection<T>.toWatchableSet(scope: CoroutineScope) = toWatchableSet(scope.coroutineContext)

/** Return a new [WatchableSet] containing [values], living on this [CoroutineScope]. */
fun <T : Any> CoroutineScope.watchableSetOf(vararg values: T) = values.toList().toWatchableSet(this)

/** Return a new [WatchableList] containing the elements of this [Collection], living on this [context]. */
fun <T : Any> Collection<T>.toWatchableList(context: CoroutineContext) = WatchableList(context, this)

/** Return a new [WatchableList] containing the elements of this [Collection], living on this [scope]. */
fun <T : Any> Collection<T>.toWatchableList(scope: CoroutineScope) = toWatchableList(scope.coroutineContext)

/** Return a new [WatchableList] containing [values], living on this [CoroutineScope]. */
fun <T : Any> CoroutineScope.watchableListOf(vararg values: T) = values.toList().toWatchableList(this)

/** Return a new [WatchableMap] containing the elements of this [Map]. */
fun <K, V> Map<K, V>.toWatchableMap(context: CoroutineContext) = WatchableMap(context, this)

/** Return a new [WatchableMap] containing the elements of this [Map]. */
fun <K, V> Map<K, V>.toWatchableMap(scope: CoroutineScope) = toWatchableMap(scope.coroutineContext)

/** Return a new [WatchableMap] containing a map of [values], living on this [CoroutineScope]. */
fun <K, V> CoroutineScope.watchableMapOf(vararg values: Pair<K, V>) = values.toMap().toWatchableMap(this)

/**
 * For a given receive channel of lists of items, emit combined lists of items no more frequently than every
 * periodMillis, starting now.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
fun <U> CoroutineScope.batch(input: ReceiveChannel<List<U>>, periodMillis: Long): ReceiveChannel<List<U>> =
    produce {
        try {
            var lastSend = System.currentTimeMillis()
            val buffer = mutableListOf<U>()
            while (true) {
                val received = input.receive()
                val toDelay = periodMillis - (System.currentTimeMillis() - lastSend)
                val toDeliver: List<U> = if (toDelay <= 0) received else {
                    // Delay until min period is reached
                    delay(toDelay)
                    if (input.isEmpty) {
                        // No further changes so send the original list as-is
                        received
                    } else {
                        // Other changes, so combine into temporary buffer
                        buffer.addAll(received)
                        while (!input.isEmpty) {
                            buffer.addAll(input.poll()!!)
                        }
                        buffer.toList().also { buffer.clear() }
                    }
                }
                lastSend = System.currentTimeMillis()
                send(toDeliver)
            }
        } catch (e: ClosedReceiveChannelException) { } // Ignore
    }

