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
sealed class MapChange<K, V> : HasSimpleChange<MapChange.Simple<K, V>> {
    override val isInitial = false

    /** The initial state of the map. */
    data class Initial<K, V>(val map: Map<K, V>) : MapChange<K, V>() {
        override val isInitial = true
        override val simple by lazy {
            map.entries.map { Simple(key = it.key, add = it.value, isInitial = true) }
        }
    }

    /** The addition of value [add], replacing value [remove] if present, for [key]. */
    data class Put<K, V>(
        val key: K,
        /** Old value for [key] if any. */
        val remove: V? = null,
        /** New value for [key]. */
        val add: V
    ) : MapChange<K, V>() {
        override val simple by lazy { listOf(Simple(key, remove, add)) }
    }

    /** A removal of item [remove] for [key]. */
    data class Remove<K, V>(
        val key: K,
        val remove: V
    ) : MapChange<K, V>() {
        override val simple by lazy { listOf(Simple(key, remove)) }
    }

    /** The simplified form of a [MapChange]. */
    data class Simple<K, V>(
        /** Key at which a change occurred. */
        val key: K,
        /** Value removed or replaced for [key] if any. */
        val remove: V? = null,
        /** Value added or updated for [key] if any. */
        val add: V? = null,
        /** True if this change came from an initial change. */
        val isInitial: Boolean = false
    )
}
