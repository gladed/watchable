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

interface MutableValue<T> : Value<T> {
    override var value: T
}

interface Value<T> {
    val value: T
}

/** A [Watchable] value of [T] which may also be replaced or bound. Use [watchableValueOf] to create. */
class WatchableValue<T> internal constructor(
    initial: T
) : MutableWatchableBase<Value<T>, T, MutableValue<T>, ValueChange<T>>(), ReadOnlyWatchableValue<T> {

    /** A holder for data. */
    private data class ValueData<T>(override val value: T) : Value<T> {
        override fun toString() = value.toString()
    }

    override var immutable: Value<T> = ValueData(initial)

    override var mutable: MutableValue<T> = object : MutableValue<T> {
        override var value: T = initial
            set(value) {
                doChange {
                    changes += ValueChange(value)
                    field = value
                }
            }
    }

    /** The currently contained value. */
    override val value: T get() = immutable.value

    override fun MutableValue<T>.toImmutable(): Value<T> = ValueData(value)

    override fun Value<T>.toInitialChange(): ValueChange<T> = ValueChange(value)

    override fun MutableValue<T>.applyBoundChange(change: ValueChange<T>) {
        value = change.value
    }

    /** Insert a new value, replacing the old one. */
    suspend fun set(value: T) {
        use {
            mutable.value = value
        }
    }

    override suspend fun clear() {
        // Clear doesn't really make sense for this data type but it is required by bind
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this {
        override fun toString() = "ReadOnlyWatchableValue($value)"
    }

    override fun equals(other: Any?) =
        when (other) {
            is WatchableValue<*> -> value == other.value
            is Value<*> -> value == other.value
            else -> value == other
        }

    override fun hashCode() = value.hashCode()
    override fun toString() = "WatchableValue($value.value)"
}
