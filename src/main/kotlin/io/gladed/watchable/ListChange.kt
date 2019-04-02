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
sealed class ListChange<T> : Change {
    abstract val simple: List<Simple<T>>

    /** An insertion of elements to the list at a particular index. */
    data class Add<T>(val index: Int, val added: List<T>) : ListChange<T>() {
        override val simple by lazy {
            added.mapIndexed { addIndex, value -> Simple(index + addIndex, value) }
        }
    }

    /** A removal of elements at one or more locations */
    data class Remove<T>(val index: Int) : ListChange<T>() {
        override val simple by lazy {
            listOf(Simple<T>(index, add = null))
        }
    }

    /** A replacement of a contiguous section of elements starting at a certain index. */
    data class Replace<T>(val index: Int, val replaced: T) : ListChange<T>() {
        override val simple by lazy {
            listOf(Simple(index, replaced))
        }
    }

    data class Simple<T>(val index: Int, val add: T? = null)
}
