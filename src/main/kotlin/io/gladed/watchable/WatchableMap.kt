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

/** A [Watchable] wrapper for a [Map] which may also be modified or bound. Use [watchableMapOf] to create.*/
@Suppress("TooManyFunctions")
class WatchableMap<K, V> internal constructor(
    initial: Map<K, V>
) : MutableWatchableBase<Map<K, V>, V, MutableMap<K, V>, MapChange<K, V>>(), ReadOnlyWatchableMap<K, V> {

    override val entries get() = value.entries
    override val keys get() = value.keys
    override val size get() = value.size
    override val values get() = value.values
    override fun containsKey(key: K) = value.containsKey(key)
    override fun containsValue(value: V) = this.value.containsValue(value)
    override fun get(key: K): V? = value[key]
    override fun isEmpty() = value.isEmpty()
    override fun equals(other: Any?) = value == other
    override fun hashCode() = value.hashCode()
    override fun toString() = "WatchableMap($value)"

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

                                    // Override equals/hash to compare with the original object
                                    override fun equals(other: Any?) = original == other
                                    override fun hashCode() = original.hashCode()
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

    /**
     * Associate the [value] with the [key] in this map, returning the previous value for this [key] if any.
     */
    suspend fun put(key: K, value: V): V? = use { put(key, value) }

    /**
     * Remove the value associated with [key], returning it if it was present.
     */
    suspend fun remove(key: K): V? = use { remove(key) }

    /** Clear all values from this map. */
    suspend fun clear() = use { clear() }

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
        override fun toString() = "ReadOnlyWatchableMap($value)"
    }
}
