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

import io.gladed.watchable.util.Guard
import io.gladed.watchable.util.guard

/** Internal implementation of [WatchableValue]. */
internal class WatchableValueBase<T>(
    initial: T
) : MutableWatchableBase<Value<T>, MutableValue<T>, ValueChange<T>>(), WatchableValue<T> {

    /** A holder for data. */
    private data class ValueData<T>(override val value: T) : Value<T>

    override var immutable: Value<T> = ValueData(initial)

    override var mutable: Guard<MutableValue<T>> = object : MutableValue<T> {
        override var value: T = initial
            set(value) {
                doChange {
                    record(ValueChange(field, value))
                    field = value
                }
            }
    }.guard()

    /** The currently contained value. */
    override val value: T get() = immutable.value

    override fun MutableValue<T>.toImmutable(): Value<T> = ValueData(value)

    override fun Value<T>.toInitialChange(): ValueChange<T> = ValueChange(null, value, true)

    override fun MutableValue<T>.applyBoundChange(change: ValueChange<T>) {
        value = change.value
    }

    /** Insert a new value, replacing the old one. */
    override suspend fun set(value: T) {
        invoke {
            this.value = value
        }
    }

    override fun MutableValue<T>.erase() {
        // Cannot erase so do nothing (bind will overwrite)
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    override fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this { }

    override fun equals(other: Any?) =
        when (other) {
            is WatchableValue<*> -> value == other.value
            is Value<*> -> value == other.value
            else -> value == other
        }

    override fun hashCode() = value.hashCode()
    override fun toString() = "$value"
}
