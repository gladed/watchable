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

/** Return a new [WatchableValue] wrapping [value]. */
fun <T> watchableValueOf(value: T) = WatchableValue(value)

/** Convert this [T] to a watchable value of [T]. */
fun <T> T.toWatchableValue() = WatchableValue(this)

/** Return a new [WatchableSet] containing the elements of this [Collection]. */
fun <T> Collection<T>.toWatchableSet() = WatchableSet(this)

/** Return a new [WatchableSet] containing [values]. */
fun <T> watchableSetOf(vararg values: T) = values.toSet().toWatchableSet()

/** Return a new [WatchableList] containing the elements of this [Collection]. */
fun <T> Collection<T>.toWatchableList() = WatchableList(this)

/** Return a new [WatchableList] containing [values]. */
fun <T> watchableListOf(vararg values: T) = values.toList().toWatchableList()

/** Return a new [WatchableMap] containing the elements of this [Map]. */
fun <K, V> Map<K, V>.toWatchableMap() = WatchableMap(this)

/** Return a new [WatchableMap] containing a map of [values]. */
fun <K, V> watchableMapOf(vararg values: Pair<K, V>) = values.toMap().toWatchableMap()

/** Create and return a group of watchable objects that itself is watchable. */
fun <C : Change> group(vararg watchables: Watchable<C>) =
    WatchableGroup(watchables.toList())
