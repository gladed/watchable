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

interface MutableContains<T> : Contains<T> {
    override var value: T
}

interface Contains<T> {
    val value: T
}

/** A [Watchable] value of [T] which may also be replaced or bound. Use [watchableValueOf] to create. */
class WatchableValue<T> internal constructor(
    initial: T
) : MutableWatchableBase<Contains<T>, T, MutableContains<T>, ValueChange<T>>(), ReadOnlyWatchableValue<T> {

    override var mutable: MutableContains<T> = object : MutableContains<T> {
        // TODO: Launch changes when this happens
        override var value: T = initial
    }

    /** Direct access to the current object. */
    override val value: Contains<T> get() = mutable

    override fun MutableContains<T>.toImmutable(): Contains<T> = object : Contains<T> {
        override val value = this@toImmutable.value
    }

    override fun Contains<T>.toInitialChange(): ValueChange<T> = ValueChange(value)

    override fun MutableContains<T>.applyBoundChange(change: ValueChange<T>) {
        value = change.value
    }

    //TODO: This can't be set() because that must take a Contains<T> which is terrible-looking
    suspend fun assign(value: T) {
        // TODO: Deal with all
        mutable.value = value
    }
    /**
     * Completely replace the contents of this watchable.
     */
    override suspend fun set(value: Contains<T>) {
        // TODO: Deal with all
        mutable.value = value.value
    }

    override fun replace(newValue: Contains<T>) {
        mutable.value = newValue.value
    }

    override suspend fun clear() {
        // Clear doesn't really make sense for this data type but it is required by bind
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this {
        override fun toString() = "ReadOnlyWatchableValue($value)"
    }

    override fun equals(other: Any?) =
        if (other is WatchableValue<*>) value == other.value else value == other
    override fun hashCode() = value.hashCode()
    override fun toString() = "WatchableValue($value)"
}
