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
sealed class SetChange<T> : Change<Set<T>, T> {
    /** The initial state of the set at the time watching began. */
    data class Initial<T>(val initial: Set<T>) : SetChange<T>() {
        override val simple by lazy { initial.map { SimpleChange(add = it) } }
    }

    /** A change representing the addition of an element to the set. */
    data class Add<T>(val added: T) : SetChange<T>() {
        override val simple by lazy { listOf(SimpleChange(add = added)) }
    }

    /** A change representing the removal of an element from the set. */
    data class Remove<T>(val removed: T) : SetChange<T>() {
        override val simple by lazy { listOf(SimpleChange(remove = removed)) }
    }
}
