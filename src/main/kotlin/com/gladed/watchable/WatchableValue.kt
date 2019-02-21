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
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/** An value which can change at any time. */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableValue<T>(
    override val coroutineContext: CoroutineContext,
    initialValue: T
) : ReadOnlyWatchableValue<T>, Bindable<T, ValueChange<T>>, CoroutineScope {

    /** The current value of the underlying object. */
    @Volatile override var value: T = initialValue
        set(value) {
            synchronized(this) {
                binder.checkChange()
                if (field == value) return
                field.also { old ->
                    field = value
                    launch {
                        channel.send(ValueChange(value, old))
                    }
                }
            }
        }

    /** Channel to receive all change messages. */
    private val channel by lazy {
        BroadcastChannel<ValueChange<T>>(CAPACITY).also { cancelWithScope(it) }
    }

    override fun watch(scope: CoroutineScope, block: (ValueChange<T>) -> Unit): Job {
        val initialValue = value

        // Create a subscription so that no further changes are missed
        val changes = channel.openSubscription()

        return scope.launch {
            // Send initial immediately, and wait for it to be processed on the target scope
            block(ValueChange(initialValue, initialValue))
            changes.consumeEach {
                block(it)
            }
        }
    }

    /** Return an unmodifiable form of this [WatchableValue]. */
    fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this {
        override fun toString() =
            "ReadOnlyWatchableValue($value)"
    }

    private val binder = BindableBase(this) {
        value = it.newValue
    }

    override val boundTo
        get() = binder.boundTo

    override fun bind(other: Watchable<T, ValueChange<T>>) {
        binder.bind(other)
    }

    override fun unbind() {
        binder.unbind()
    }

    override fun toString() =
        "WatchableValue($value)"

    companion object {
        private const val CAPACITY = 20
    }
}
