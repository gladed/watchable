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

import com.gladed.watchable.LocalScope
import com.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.lang.IllegalArgumentException
import java.util.concurrent.Executors

class BindValueTest {
    @Test fun bind() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            dest.bind(origin)
            yield()
            yield()
            assertEquals(5, dest.value)
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            dest.bind(origin)
            yield()
            yield()
            origin.value = 7
            yield()
            yield()
            assertEquals(7, dest.value)
        }
    }

    @Test fun badBind() {
        try {
            runThenCancel {
                val origin = watchableValueOf(5)
                origin.bind(origin)
                fail("Shouldn't get here")
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun badRebind() {
        try {
            runThenCancel {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                dest.bind(origin)
                dest.bind(origin)
                fail("Second bind should have thrown")
            }
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test fun badCircle() {
        try {
            runThenCancel {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                dest.bind(origin)
                origin.bind(dest) // Circular binding
                fail("Second bind should have thrown")
            }
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test fun badModify() {
        try {
            runBlocking {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                dest.bind(origin)
                dest.value = 7
                fail("Modification should not be permitted")
                yield()
                yield()
                assertEquals(6, dest.value)
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbind() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            dest.bind(origin)
            yield()
            yield()
            dest.unbind()
            origin.value = 7
            yield()
            assertEquals(5, dest.value)
        }
    }

    private val dispatcher1 = Executors.newSingleThreadExecutor {
        task -> Thread(task, "dispatcher1")
    }.asCoroutineDispatcher()
    private val scope1 = LocalScope(dispatcher1)

    private val dispatcher2 = Executors.newSingleThreadExecutor {
        task -> Thread(task, "dispatcher2")
    }.asCoroutineDispatcher()
    private val scope2 = LocalScope(dispatcher2)

    @Test fun killDestScope() {
        val origin = scope1.watchableValueOf(5)
        val dest = scope2.watchableValueOf(6)
        runBlocking {
            dest.bind(origin)
            origin.value = 7
            delay(50) // Let scopes do their thing
            assertEquals(7, dest.value)
            scope2.close() // Kill the destination value's scope
            delay(50)
            origin.value = 8
            delay(50)
        }
        assertEquals(7, dest.value)
    }

    @Test fun killOriginScope() {
        val origin = scope1.watchableValueOf(5)
        val dest = scope2.watchableValueOf(6)
        runBlocking {
            dest.bind(origin)
            origin.value = 7
            delay(50) // Let scopes do their thing
            scope1.close() // Kill the origin value's scope
            origin.value = 8
            delay(50)
        }
        assertEquals(7, dest.value)
    }
}