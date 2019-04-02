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

/** Describes a change in the simplest possible form. */
data class SimpleChange<out K, out V>(
    /** The site of the change. */
    val location: K,
    /** The item added, or null if removed. */
    val add: V? = null
) {
    /** Return true if this change corresponds to the remove of the element at [location]. */
    val isRemove get() = add == null

    /** Return true if this change corresponds to an addition of [add] at [location]. */
    val isAdd get() = !isRemove
}
