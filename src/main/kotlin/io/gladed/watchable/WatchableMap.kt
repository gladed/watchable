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
 * A [Map] whose contents may be watched for changes.
 */
@UseExperimental(kotlinx.coroutines.ObsoleteCoroutinesApi::class,
    kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WatchableMap<K, V>(
    override val coroutineContext: CoroutineContext,
    initial: Map<K, V>
) : MutableWatchableBase<MutableMap<K, V>, Map<K, V>, MapChange<K, V>>(), ReadOnlyWatchableMap<K, V> {

    /** A map that checks and reports all change attempts. */
    override val mutable = object : AbstractMutableMap<K, V>() {
        private val real = initial.toMutableMap()

        override val entries: MutableSet<MutableMap.MutableEntry<K, V>> =
            object : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
                override val size: Int get() = real.size

                // Kotlin MutableMap doesn't support this so we won't either
                override fun add(element: MutableMap.MutableEntry<K, V>) = throw UnsupportedOperationException()

                override fun iterator() = object : MutableIterator<MutableMap.MutableEntry<K, V>> {
                    val realIterator = real.iterator()
                    var last: MutableMap.MutableEntry<K, V>? = null

                    override fun hasNext() = realIterator.hasNext()

                    override fun next(): MutableMap.MutableEntry<K, V> =
                        realIterator.next()
                            // Cache last so that it can be removed if necessary
                            .also { last = it }
                            // Wrap the returned entry so that we can detect changes on it
                            .let { original ->
                                object : MutableMap.MutableEntry<K, V> by original {
                                    override fun setValue(newValue: V): V = doChange {
                                        original.setValue(newValue).also {
                                            changes += MapChange.Replace(key, it, newValue)
                                        }
                                    }

                                    // Override equals to compare with the original object
                                    override fun equals(other: Any?) = original == other

                                    override fun hashCode() = original.hashCode()
                                    // Print nicely
                                    override fun toString() = "($key, $value)"
                                }
                            }

                    override fun remove() {
                        doChange {
                            realIterator.remove()
                            // Last cannot be null if remove() succeeded
                            changes += MapChange.Remove(last!!.key, last!!.value)
                        }
                    }
                }
            }

        override fun put(key: K, value: V): V? = doChange {
            real.put(key, value).also { oldValue ->
                if (oldValue == null) {
                    changes += MapChange.Add(key, value)
                } else {
                    changes += MapChange.Replace(key, oldValue, value)
                }
            }
        }
    }

    override fun MutableMap<K, V>.toImmutable() = toMap()

    override fun Map<K, V>.toInitialChange() = MapChange.Initial(this)

    override fun MutableMap<K, V>.applyBoundChange(change: MapChange<K, V>) {
        when (change) {
            is MapChange.Initial -> {
                clear()
                putAll(change.initial)
            }
            is MapChange.Add -> put(change.key, change.added)
            is MapChange.Remove -> remove(change.key)
            is MapChange.Replace -> put(change.key, change.added)
        }
    }

    override fun replace(newValue: Map<K, V>) {
        mutable.clear()
        mutable.putAll(newValue)
    }

    /** Return an unmodifiable form of this [WatchableMap]. */
    fun readOnly(): ReadOnlyWatchableMap<K, V> = object : ReadOnlyWatchableMap<K, V> by this {
        override fun toString() = "ReadOnlyWatchableMap()"
    }

    override fun toString() = "WatchableMap()"
}
