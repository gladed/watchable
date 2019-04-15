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
    override var immutable: Map<K, V> = initial.toMap()

    override val entries get() = immutable.entries
    override val keys get() = immutable.keys
    override val size get() = immutable.size
    override val values get() = immutable.values
    override fun containsKey(key: K) = immutable.containsKey(key)
    override fun containsValue(value: V) = immutable.containsValue(value)
    override fun get(key: K): V? = immutable[key]
    override fun isEmpty() = immutable.isEmpty()
    override fun equals(other: Any?) = immutable == other
    override fun hashCode() = immutable.hashCode()
    override fun toString() = "$immutable"

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
                                            record(MapChange.Put(key, remove = value, add = newValue))
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
                            last?.also { record(MapChange.Remove(it.key, it.value)) }
                        }
                    }
                }
            }

        override fun put(key: K, value: V): V? = doChange {
            doChange {
                real.put(key, value).also { oldValue ->
                    record(MapChange.Put(key, remove = oldValue, add = value))
                }
            }
        }
    }

    /** Associate the [value] with the [key] in this map, returning the previous value for this [key] if any. */
    suspend inline fun put(key: K, value: V): V? = use { put(key, value) }

    suspend inline fun putAll(from: Map<K, V>) {
        use { putAll(from) }
    }

    suspend inline fun putAll(pairs: Array<out Pair<K, V>>) {
        use { putAll(pairs) }
    }

    suspend inline fun putAll(pairs: Iterable<Pair<K, V>>) {
        use { putAll(pairs) }
    }

    suspend inline fun putAll(pairs: Sequence<Pair<K, V>>) {
        use { putAll(pairs) }
    }

    suspend inline operator fun plusAssign(from: Pair<K, V>) { put(from.first, from.second) }

    suspend inline operator fun plusAssign(from: Map<K, V>) { putAll(from) }

    suspend inline operator fun plusAssign(pairs: Array<out Pair<K, V>>) { putAll(pairs) }

    suspend inline operator fun plusAssign(pairs: Iterable<Pair<K, V>>) { putAll(pairs) }

    suspend inline operator fun plusAssign(pairs: Sequence<Pair<K, V>>) { putAll(pairs) }

    suspend inline operator fun minusAssign(key: K) { remove(key) }

    suspend inline operator fun minusAssign(pairs: Array<K>) {
        use { keys.removeAll(pairs) }
    }

    suspend inline operator fun minusAssign(pairs: Iterable<K>) {
        use { keys.removeAll(pairs) }
    }

    suspend inline operator fun minusAssign(pairs: Sequence<K>) {
        use { keys.removeAll(pairs) }
    }

    /** Remove the value associated with [key], returning it if it was present. */
    suspend inline fun remove(key: K): V? = use { remove(key) }

    /**  all values from this map. */
    override suspend fun clear() = use { clear() }

    override fun MutableMap<K, V>.toImmutable() = toMap()

    override fun Map<K, V>.toInitialChange() = MapChange.Initial(this)

    override fun MutableMap<K, V>.applyBoundChange(change: MapChange<K, V>) {
        when (change) {
            is MapChange.Initial -> { clear(); putAll(change.map) }
            is MapChange.Put -> put(change.key, change.add)
            is MapChange.Remove -> remove(change.key)
        }
    }

    /** Return an unmodifiable form of this [WatchableMap]. */
    fun readOnly(): ReadOnlyWatchableMap<K, V> = object : ReadOnlyWatchableMap<K, V> by this { }
}
