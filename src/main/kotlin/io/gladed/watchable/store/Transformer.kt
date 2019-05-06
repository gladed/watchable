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

package io.gladed.watchable.store

/** Convert between source ([S]) and target ([T]) forms of an object. */
interface Transformer<S : Any, T : Any> {
    /** Convert an instance of [S] to [T]. */
    fun toTarget(value: S): T

    /** Convert an instance of [T] to [S]. */
    fun fromTarget(value: T): S

    /** Combine this [Transformer] with [other] to produce a transitive [Transformer]. */
    operator fun <T2 : Any> plus(other: Transformer<T, T2>): Transformer<S, T2> = object : Transformer<S, T2> {
        override fun toTarget(value: S): T2 =
            other.toTarget(this@Transformer.toTarget(value))

        override fun fromTarget(value: T2): S =
            this@Transformer.fromTarget(other.fromTarget(value))
    }
}
