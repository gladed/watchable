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

    /** An insertion of items to the list at a particular index. */
    data class Insert<T>(val index: Int, val items: List<T>) : ListChange<T>() {
        override val simple by lazy {
            items.mapIndexed { addIndex, value -> Simple(index + addIndex, value, insert = true) }
        }
    }

    /** A removal of elements at one or more locations */
    data class Remove<T>(val range: IntRange) : ListChange<T>() {
        constructor(index: Int) : this(index..index)
        override val simple by lazy {
            range.map { Simple<T>(it) }
        }
    }

    /** Overwrites a contiguous section of elements already in the list, starting at [index]. */
    data class Replace<T>(val index: Int, val items: List<T>) : ListChange<T>() {
        override val simple by lazy {
            items.mapIndexed { replaceIndex, element -> Simple(index + replaceIndex, element, insert = false) }
        }
    }

    data class Simple<T>(
        /** Index at which a change occurred. */
        val index: Int,
        /** Item added or inserted at [index], or null if the item there was removed. */
        val item: T? = null,
        /** If true and [item] is non-null, it was inserted, or if false [item] overwrites existing value. */
        val insert: Boolean = true
    )
}
