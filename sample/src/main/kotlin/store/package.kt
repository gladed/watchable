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

package store

import kotlin.coroutines.CoroutineContext

/**
 * Return a [HoldingStore] around this [Store].
 */
fun <T : Any> Store<T>.holding(context: CoroutineContext, start: suspend (T) -> Hold) =
    HoldingStore(context, this, start)

/** Throw an exception to complain that something cannot be done. */
fun cannot(doSomething: String): Nothing = throw Cannot(doSomething)
