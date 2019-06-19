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

import io.gladed.watchable.util.Cannot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val clockTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

fun log(message: Any?) {
    val time = ZonedDateTime.now(ZoneOffset.UTC).format(clockTimeFormat)
    println("$time ${Thread.currentThread().name}: $message")
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

/** Keep garbage collecting and running [untilSuccess] until it doesn't throw or until we exhaust attempts. */
suspend fun scour(maxIterations: Int = 20, delay: Int = 10, untilSuccess: () -> Unit) {
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
    assertEquals(obj, obj)
    @Suppress("SENSELESS_COMPARISON")
    assertFalse(obj == null)
    assertFalse(obj == 1)
    assertNotNull(obj.toString())
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

@UseExperimental(ExperimentalCoroutinesApi::class)
fun runTest(func: suspend TestCoroutineScope.() -> Unit) {
    TestCoroutineScope(TestCoroutineDispatcher() + Job()).runBlockingTest {
        func()
    }
}

@UseExperimental(ObsoleteCoroutinesApi::class)
suspend fun <C> ReceiveChannel<C>.mustBe(vararg items: C) {
    if (items.isEmpty()) {
        assertEquals(null, poll())
    } else {
        for (item in items) {
            val rx = withTimeoutOrNull(150) { receiveOrNull() }
            log("Rx: ${rx ?: "timeout"}")
            assertEquals(item, rx)
        }
    }
}

suspend fun impossible(func: suspend () -> Unit) {
    try {
        func()
        fail("should have failed")
    } catch (c: Cannot) {
        println("As expected: $c")
    }
}
