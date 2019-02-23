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

package com.gladed.watchable

/** Describes a change to a [Map]. */
sealed class MapChange<K, V> : Change<Map<K, V>> {
    /** The initial state of the map at the time watching began. */
    data class Initial<K, V>(val initial: Map<K, V>) : MapChange<K, V>()

    /** A change representing the addition of a new value for a key in the map. */
    data class Add<K, V>(val key: K, val added: V) : MapChange<K, V>()

    /** A change representing the removal of an element for a key in the map. */
    data class Remove<K, V>(val key: K, val removed: V) : MapChange<K, V>()

    /** A change representing the replacement of an element for a key in the map. */
    data class Replace<K, V>(val key: K, val removed: V, val added: V) : MapChange<K, V>()
}
