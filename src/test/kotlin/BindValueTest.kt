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

import io.gladed.watchable.ValueChange
import io.gladed.watchable.watch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThat
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class BindValueTest {
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun bindTest() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            dest.bind(origin)
            eventually { assertEquals(5, dest.get()) }
        }
    }

    @Test fun bindThenChange() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            watch(dest) { changes += it }
            changes.expect(ValueChange(6, 6))
            dest.bind(origin)
            origin.set(7)
            changes.expect(ValueChange(6, 7))
            assertEquals(7, dest.get())
        }
    }

    @Test fun badBind() {
        try {
            runBlocking {
                val origin = watchableValueOf(5)
                origin.bind(origin)
                fail("Shouldn't get here")
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun badRebind() {
        try {
            runBlocking {
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
            runBlocking {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                dest.bind(origin)
                origin.bind(dest)
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
                dest.set(7)
                fail("Modification should not be permitted")
                assertEquals(6, dest.get())
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbind() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            dest.bind(origin)
            eventually { assertEquals(5, dest.get()) }
            dest.unbind()
            origin.set(7)
            always { assertEquals(5, dest.get()) }
        }
    }

    private val dispatcher1 = Executors.newSingleThreadExecutor {
        task -> Thread(task, "scope1")
    }.asCoroutineDispatcher()
    private val scope1 = LocalScope(dispatcher1)

    private val dispatcher2 = Executors.newSingleThreadExecutor {
        task -> Thread(task, "scope2")
    }.asCoroutineDispatcher()
    private val scope2 = LocalScope(dispatcher2)

    @Test fun watchOnScope() {
        runBlocking {
            val origin = scope1.watchableValueOf(5)
            scope2.watch(origin) {
                assertThat(Thread.currentThread().name, containsString("scope2"))
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            origin.set(6)
            changes.expect(ValueChange(5, 6))
        }
    }

    @Test fun killDestScope() {
        runBlocking {
            val origin = scope1.watchableValueOf(5)
            val dest = scope2.watchableValueOf(6)
            scope2.watch(dest) { changes += it }
            dest.bind(origin)
            origin.set(7)
            eventually { assertEquals(7, dest.get()) }
            scope2.close() // Kill the destination value's scope

            origin.set(8)
            always { assertEquals(7, dest.get()) }
        }
    }

    @Test fun killOriginScope() {
        runBlocking {
            val origin = scope1.watchableValueOf(5)
            val dest = scope2.watchableValueOf(6)
            watch(dest) {
                changes += it
            }
            changes.expect(ValueChange(6, 6))
            dest.bind(origin)
            changes.expect(ValueChange(6, 5))
            origin.set(7)
            changes.expect(ValueChange(5, 7))

            scope1.close() // Kill the origin value's scope
            assertFalse(origin.isActive)
            // Because origin scope was killed it should NOT pass further values on to dest
            origin.set(8)
            changes.expectNone()
            assertEquals(7, dest.get())
        }
    }
}
