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
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.whileSelect
import java.time.Clock

/**
 * Return a channel which receives items from [input] no more than once per [periodMillis], except
 * at close of input in which all outstanding contents are delivered immediately before close.
 *
 * ```
 * p = period expiration
 *
 * In:  |--[A]--------[B,C]--[D]---------------[E]--[F]--|
 * Out:        |[A]---------------p[B,C,D]--p--[E]-------[F]|
 * ```
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal fun <C> CoroutineScope.collect(
    input: ReceiveChannel<List<C>>,
    periodMillis: Long = 0,
    initial: List<C> = listOf(),
    clock: Clock = Clock.systemUTC()
): ReceiveChannel<List<C>> = produce {
    var lastSend = 0L
    val buffer = initial.toMutableList()

    // If this scope dies be sure to close our channel
    invokeOnClose { input.cancel() }

    fun millisRemaining() = periodMillis - (clock.millis() - lastSend)

    // Send everything currently in buffer, if any
    suspend fun deliver(force: Boolean = false) {
        while (true) input.poll()?.also { buffer += it } ?: break
        if (buffer.isNotEmpty() && (millisRemaining() <= 0 || force)) {
            send(buffer.toList())
            lastSend = clock.millis()
            buffer.clear()
        }
    }

    deliver(false) // Send initial if any

    whileSelect {
        input.onReceiveOrNull {
            // If we receive data collect it
            if (it != null) {
                buffer += it
                deliver(false)
                true
            } else {
                lastSend = 0
                deliver(true)
                false // Always exit in this case since input is closed.
            }
        }

        millisRemaining().also {
            if (it > 0) onTimeout(it) {
                deliver(false)
                true
            }
        }
    }
}
