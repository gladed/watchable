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

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking

/** Create a scope, run the block() in it, then cancel the scope. */
fun <T> runThenCancel(block: suspend CoroutineScope.() -> T) {
    try {
        runBlocking {
            block().also {
                coroutineContext.cancel()
            }
        }
    } catch (e: CancellationException) {
        // As expected
    }
}

fun log(message: String) {
    println(Thread.currentThread().name + ": $message")
}
