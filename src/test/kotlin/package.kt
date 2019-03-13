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
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert

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

fun <T, M : T, C : Change<T>> CoroutineScope.iterateMutable(
    one: MutableWatchable<T, M, C>,
    two: MutableWatchable<T, M, C>,
    mods: List<M.() -> Unit>,
    closer: M.() -> Unit,
    chooser: Chooser,
    count: Int = 1000
): Job = launch {

    bind(two, one)
    watch(two) {
        launch {
            if (0 == chooser(10)) two.get()
        }
    }
    log("Launching $count")

    (0 until count).map {
        launch {
            one.use {
                chooser(mods)!!(this)
            }
        }
    }.joinAll()

    log("Closing")
    one.use { closer() }

    watch(two) {
        launch {
            if (one.get() == two.get()) {
                this@iterateMutable.coroutineContext.cancel()
                yield()
            }
        }
    }
    delay(3000)
    Assert.assertEquals(one.get(), two.get())
}

suspend fun eventually(timeout: Int = 250, delay: Int = 10, test: suspend () -> Unit) {
    try {
        withTimeout(timeout.toLong()) {
            while (true) {
                try {
                    test()
                    break
                } catch (e: Error) {
                    delay(delay.toLong())
                    continue
                }
            }
        }
    } catch (e: TimeoutCancellationException) {
        // Do a final test to quietly return (happy) or throw (sad)
        test()
    }
}

suspend fun always(timeout: Int = 100, delay: Int = 10, test: suspend () -> Unit) {
    try {
        withTimeout(timeout.toLong()) {
            // Yield until it gets to what we want
            while (isActive) {
                test()
                delay(delay.toLong())
            }
        }
    } catch (e: TimeoutCancellationException) {  } // expected
}

/** Keep garbage collecting and running [untilSuccess] until it doesn't throw or until we exhaust attempts. */
suspend fun scour(maxIterations: Int = 50, delay: Int = 50, untilSuccess: () -> Unit) {
    val runtime = Runtime.getRuntime()
    for (i in 0 until maxIterations) {
        runtime.runFinalization()
        runtime.gc()
        try {
            untilSuccess()
            return
        } catch (e: AssertionError) { } // Keep trying
        delay(delay.toLong())
    }
    untilSuccess()
}
