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

import io.gladed.watchable.Change
import io.gladed.watchable.MutableWatchable
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert

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

fun log(message: Any?) {
    println(Thread.currentThread().name + ": $message")
}

/** Launch block on the current scope then block until it completes. */
fun CoroutineScope.runToEnd(block: suspend () -> Unit) {
    launch { block() }.also {
        runBlocking {
            it.join()
        }
    }.invokeOnCompletion { cause ->
        if (cause != null && cause !is CancellationException) throw cause
    }
}

suspend fun <M : T, T, C : Change<T>> iterateMutable(
    scope: CoroutineScope,
    one: MutableWatchable<M, T, C>,
    two: MutableWatchable<M, T, C>,
    mods: List<M.() -> Unit>,
    closer: M.() -> Unit,
    chooser: Chooser,
    count: Int = 1000) {

    scope.bind(one, two)
    scope.watch(two) {
        scope.launch {
            if (0 == chooser(10)) two.get()
        }
    }

    (0 until count).map {
        scope.launch {
            one.use {
                chooser(mods)!!(this)
            }
        }
    }.joinAll()

    println("----sending closer----")
    one.use { closer() }

    scope.watch(two) {
        scope.launch {
            if (one.get() == two.get()) {
                println("DONE, cancelling")
                scope.coroutineContext.cancel()
                yield()
            }
        }
    }
    delay(3000)
    Assert.assertEquals(one.get(), two.get())
}
