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

/** Describes a change to a [Map]. */
sealed class MapChange<K, out V> : Change {
    abstract val simple: List<Simple<K, V>>

    /** The addition or replacement of values for keys in the map. */
    data class Put<K, V>(val pairs: Collection<Pair<K, V>>) : MapChange<K, V>() {
        override val simple by lazy {
            pairs.map { (key, value) -> Simple(key, value) }
        }
    }

    /** The removal of elements corresponding to keys in the map. */
    data class Remove<K, V>(val removed: Collection<K>) : MapChange<K, V>() {
        override val simple by lazy {
            removed.map { key -> Simple<K, V>(key) }
        }
    }

    data class Simple<K, out V>(val key: K, val value: V? = null)
}
