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

/** Describes a change to a [Set]. */
sealed class SetChange<T> : HasSimpleChange<SetChange.Simple<T>> {

    /** The initial state of the set. */
    data class Initial<T>(val set: Set<T>) : SetChange<T>() {
        override val simple by lazy { set.map { Simple(add = it) } }
    }

    /** An addition of items. */
    data class Add<T>(val add: List<T>) : SetChange<T>(), Addable<SetChange<T>> {
        override val simple by lazy { add.map { Simple(add = it) } }
        override operator fun plus(other: SetChange<T>) =
            if (other is Add) Add(add + other.add) else null
    }

    /** A removal of items. */
    data class Remove<T>(val remove: List<T>) : SetChange<T>(), Addable<SetChange<T>> {
        override val simple by lazy { remove.map { Simple(remove = it) } }
        override operator fun plus(other: SetChange<T>) =
            if (other is Remove) Remove(remove + other.remove) else null
    }

    /** The simplest form of a set change, affecting only a single item (either [add] or [remove]). */
    data class Simple<T>(val add: T? = null, val remove: T? = null)
}
