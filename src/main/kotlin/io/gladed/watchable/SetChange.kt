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
data class SetChange<T>(
    val added: Collection<T> = emptyList(),
    val removed: Collection<T> = emptyList()
) : Change {

    /** This change, in terms of [Simple] changes. */
    val simple: List<Simple<T>> by lazy {
        removed.map { Simple(it, false) } + added.map { Simple(it, true) }
    }

    /** The simplest possible explanation of a change. */
    data class Simple<T>(val value: T, val add: Boolean) {
        val remove get() = !add
    }
}
