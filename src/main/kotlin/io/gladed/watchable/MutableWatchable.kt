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

/**
 * A [Watchable] wrapping type [T] which may also be mutated in the form of an [M].
 */
interface MutableWatchable<M: T, T, C : Change<T>> : Watchable<T, C>, Bindable<T, C> {
    /**
     * Suspend until [func] can safely execute, reading and/or writing data on [M] as desired and returning
     * the result. Note: if currently bound ([isBound] returns true), attempts to modify [M] will throw.
     */
    suspend fun <U> use(func: suspend M.() -> U): U

    /**
     * Completely replace the contents of this watchable.
     */
    suspend fun set(value: T)
}
