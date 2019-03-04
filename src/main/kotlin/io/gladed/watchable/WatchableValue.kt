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

import kotlin.coroutines.CoroutineContext

/**
 * A wrapper for a value which may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableValue<T> internal constructor(
    override val coroutineContext: CoroutineContext,
    initial: T
) : MutableWatchableBase<T, T, ValueChange<T>>(), ReadOnlyWatchableValue<T> {

    override var mutable: T = initial

    /** Direct access to the current object. */
    override val value: T get() = mutable

    override fun T.toImmutable(): T = this

    override fun T.toInitialChange() = ValueChange(this, this)

    override fun T.applyBoundChange(change: ValueChange<T>) {
        replace(change.newValue)
    }

    override fun replace(newValue: T) {
        val oldValue = mutable
        mutable = newValue
        changes += listOf(ValueChange(oldValue, newValue))
    }

    /** Return an unmodifiable form of this [WatchableSet]. */
    fun readOnly(): ReadOnlyWatchableValue<T> = object : ReadOnlyWatchableValue<T> by this {
        override fun toString() = "ReadOnlyWatchableValue()"
    }

    override fun toString() = "WatchableValue()"
}
