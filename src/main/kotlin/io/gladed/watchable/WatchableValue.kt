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
import kotlin.coroutines.CoroutineContext

/**
 * A mutable value which be watched for replacement of its value and/or bound to other maps for the
 * duration of its [coroutineContext].
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableValue<T>(
    override val coroutineContext: CoroutineContext,
    initialValue: T
) : ReadOnlyWatchableValue<T>, Bindable<T, ValueChange<T>>, CoroutineScope {

    /** The current value of the underlying object. */
    @Volatile override var value: T = initialValue
        set(value) {
            delegate.changeOrNull {
                if (field == value) null else {
                    val old = field
                    field = value
                    ValueChange(value, old)
                }
            }
        }

    /** A delegate implementing common functions. */
    private val delegate = object : WatchableDelegate<T, ValueChange<T>>(coroutineContext, this@WatchableValue) {
        override val initialChange
            get() = ValueChange(value, value)

        override fun onBoundChange(change: ValueChange<T>) {
            value = change.newValue
        }
    }

    override fun CoroutineScope.watchBatches(block: suspend (List<ValueChange<T>>) -> Unit) =
        delegate.watchOwnerBatch(this@watchBatches, block)

    /** Return an unmodifiable form of this [WatchableValue]. */
    fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this {
        override fun toString() =
            "ReadOnlyWatchableValue($value)"
    }

    override val boundTo: Watchable<T, ValueChange<T>>?
        get() = delegate.boundTo

    override fun bind(other: Watchable<T, ValueChange<T>>) =
        delegate.bind(other)

    override fun unbind() =
        delegate.unbind()

    override fun toString() =
        "WatchableValue($value)"
}
