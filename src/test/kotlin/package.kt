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
import io.gladed.watchable.Watchable
import io.gladed.watchable.watch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val clockTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

fun log(message: Any?) {
    val now = ZonedDateTime.now( ZoneOffset.UTC ).format( clockTimeFormat )
    println("$now ${Thread.currentThread().name}: $message")
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
suspend fun scour(maxIterations: Int = 20, delay: Int = 50, untilSuccess: () -> Unit) {
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

/** Fail if [func] doesn't throw with [cls]. */
inline fun mustThrow(cls: Class<out Any> = Throwable::class.java, func: () -> Unit) {
    try {
        func()
        fail("Did not throw!")
    } catch (t: Throwable) {
        if (!cls.isInstance(t)) {
            fail("Threw unexpected $t")
        }
    }
}

/** Call all the things on a Kotlin data class. */
fun <T : Any> cover(obj: T) {
    Assert.assertEquals(obj, obj)
    @Suppress("SENSELESS_COMPARISON")
    (Assert.assertFalse(obj == null))
    Assert.assertFalse(obj == 1)
    Assert.assertNotNull(obj.toString())
    obj.hashCode()
    obj::class.java.declaredMethods.forEach { method ->
        // Invoke all available accessors
        if (method.name.startsWith("get") && method.parameterCount == 0) {
            try {
                method.invoke(obj)
            } catch (exception: IllegalAccessException) { } // Don't care
        }

        if (method.name.startsWith("component") && method.parameterCount == 0) {
            method.invoke(obj)
        }
    }
}

/** Monitor events until func does not throw. */
suspend fun <T, V, C: Change> Watchable<T, V, C>.watchUntil(scope: CoroutineScope, func: () -> Unit) {
    val mutex = Mutex(locked = true)
    val handle = scope.watch(this) {
        try {
            func()
            mutex.unlock()
        } catch (t: Throwable) { }
    }
    mutex.lock() // Suspend until success
    handle.cancel() // Cancel listening
}

suspend fun <C> ReceiveChannel<C>.expect(vararg expected: C, timeout: Long = 250, strict: Boolean = true) {
    val expectedList = expected.toList()
    val current = mutableListOf<C>()

    val result = withTimeoutOrNull(timeout) {
        while (expectedList != current) {
            if (strict && current.isNotEmpty()) {
                Assert.assertEquals(expectedList.take(current.size), current)
            }
            current.add(receive())
            if (!strict && current.size > expectedList.size) {
                current.removeAt(0)
            }
        }
    }
    if (result == null) {
        Assert.assertEquals(expectedList, current)
    }
}

suspend fun <C> ReceiveChannel<C>.expectNone(delayMillis: Long = 25) {
    delay(delayMillis)
    assertNull(poll())
}
