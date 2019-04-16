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

/** A [Watchable] [Map] which may also be modified or bound. Use [watchableMapOf] to create.*/
@Suppress("TooManyFunctions")
interface WatchableMap<K, V> : MutableWatchable<MutableMap<K, V>, MapChange<K, V>>, ReadOnlyWatchableMap<K, V> {

    /** Associate the [value] with the [key] in this map, returning the previous value for this [key] if any. */
    suspend fun put(key: K, value: V): V? = use { put(key, value) }

    // Note: suspend operator for set would be more natural but is not yet supported by Kotlin.

    /** Put the key and value in [from] into this map. */
    suspend fun putAll(from: Map<K, V>) { use { putAll(from) } }

    /** Put all keys and values in [pairs] into this map. */
    suspend fun putAll(pairs: Array<out Pair<K, V>>) { use { putAll(pairs) } }

    /** Put all keys and values in [pairs] into this map. */
    suspend fun putAll(pairs: Iterable<Pair<K, V>>) { use { putAll(pairs) } }

    /** Put all keys and values in [pairs] into this map. */
    suspend fun putAll(pairs: Sequence<Pair<K, V>>) { use { putAll(pairs) } }

    /** Remove [key] and its value from this map, returning the former value if present. */
    suspend fun remove(key: K): V? = use { remove(key) }

    /** Put the key and value in [from] into this map. */
    suspend operator fun plusAssign(from: Pair<K, V>) { put(from.first, from.second) }

    /** Put all keys and values in [from] into this map. */
    suspend operator fun plusAssign(from: Map<K, V>) { putAll(from) }

    /** Put all keys and values in [pairs] into this map. */
    suspend operator fun plusAssign(pairs: Array<out Pair<K, V>>) { putAll(pairs) }

    /** Put all keys and values in [pairs] into this map. */
    suspend operator fun plusAssign(pairs: Iterable<Pair<K, V>>) { putAll(pairs) }

    /** Put all keys and values in [pairs] into this map. */
    suspend operator fun plusAssign(pairs: Sequence<Pair<K, V>>) { putAll(pairs) }

    /** Remove any value corresponding to [key] from this map. */
    suspend operator fun minusAssign(key: K) { remove(key) }

    /** Remove [keys] from this map, along with any associated values. */
    suspend operator fun minusAssign(removeKeys: Array<K>) { use { keys.removeAll(removeKeys) } }

    suspend operator fun minusAssign(removeKeys: Iterable<K>) { use { keys.removeAll(removeKeys) } }

    suspend operator fun minusAssign(removeKeys: Sequence<K>) { use { keys.removeAll(removeKeys) } }

    /** Return an unmodifiable form of this [WatchableMap]. */
    override fun readOnly(): ReadOnlyWatchableMap<K, V>
}
