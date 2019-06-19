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

package io.gladed.watchable.util

/**
 * The result of a suspending attempt to do something that might not be possible (e.g. could throw [Cannot]).
 */
sealed class Try<T : Any> {
    abstract val passOrNull: T?
    abstract val failOrNull: Cannot?

    data class Pass<T : Any>(val value: T) : Try<T>() {
        override val passOrNull = value
        override val failOrNull: Cannot? = null
    }

    data class Fail<T : Any>(val cannot: Cannot) : Try<T>() {
        override val passOrNull: T? = null
        override val failOrNull = cannot
    }

    companion object {
        suspend operator fun <T : Any> invoke(func: suspend () -> T): Try<T> = try {
            Pass(func())
        } catch (c: Cannot) {
            Fail(c)
        }
    }
}
