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

/** Describes a change to a [List]. */
sealed class ListChange<T> : HasSimpleChange<ListChange.Simple<T>> {

    /** The initial state of the list, delivered upon first watch. */
    data class Initial<T>(val list: List<T>) : ListChange<T>() {
        override val simple by lazy {
            list.mapIndexed { index, item -> Simple(index, add = item) }
        }
    }

    /** An insertion of items at [index]. */
    data class Insert<T>(val index: Int, val insert: List<T>) : ListChange<T>(), Addable<ListChange<T>> {
        override val simple by lazy {
            insert.mapIndexed { addIndex, item -> Simple(index + addIndex, add = item) }
        }

        override operator fun plus(other: ListChange<T>): ListChange<T>? =
            if (other is Insert && index + insert.size == other.index) {
                copy(insert = insert + other.insert)
            } else null
    }

    /** A removal of [remove] at [index]. */
    data class Remove<T>(val index: Int, val remove: T) : ListChange<T>() {
        override val simple by lazy {
            listOf(Simple(index, remove = remove))
        }
    }

    /** An overwriting of [remove] at [index], replacing with [add]. */
    data class Replace<T>(val index: Int, val remove: T, val add: T) : ListChange<T>() {
        override val simple by lazy {
            listOf(Simple(index, remove, add))
        }
    }

    /** The simplest form of a list change, affecting only a single position in the list. */
    data class Simple<T>(
        /** Index at which a change occurred. */
        val index: Int,
        /** Item removed at [index] if any. */
        val remove: T? = null,
        /** Item added at [index] if any. */
        val add: T? = null
    )
}
