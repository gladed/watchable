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
import io.gladed.watchable.bind
import io.gladed.watchable.watch
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class BindValueTest {
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun bindTest() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            bind(origin, dest)
            watch(dest) { changes += it }
            changes.expect(ValueChange(5, 5))
            assertEquals(5, dest.get())
        }
    }

    @Test fun bindThenChange() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            watch(dest) { changes += it }
            changes.expect(ValueChange(6, 6))
            bind(origin, dest)
            origin.set(7)
            changes.expect(ValueChange(6, 7))
            assertEquals(7, dest.get())
        }
    }

    @Test fun badBind() {
        try {
            runThenCancel {
                val origin = watchableValueOf(5)
                bind(origin, origin)
                fail("Shouldn't get here")
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun badRebind() {
        try {
            runThenCancel {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                bind(origin, dest)
                bind(origin, dest)
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
                bind(origin, dest)
                bind(dest, origin) // Circle
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
                bind(origin, dest)
                dest.set(7)
                fail("Modification should not be permitted")
                assertEquals(6, dest.get())
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun unbind() {
        runThenCancel {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            bind(origin, dest)
            watch(dest) { changes += it }
            changes.expect(ValueChange(5, 5))
            dest.unbind()
            origin.set(7)
            changes.expectNone()
            assertEquals(5, dest.get())
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
        runThenCancel {
            val origin = scope1.watchableValueOf(5)
            val dest = scope2.watchableValueOf(6)
            watch(dest) { changes += it }
            changes.expect(ValueChange(6, 6))
            scope2.bind(origin, dest)
            changes.expect(ValueChange(6, 5))
            origin.set(7)
            changes.expect(ValueChange(5, 7))
            assertEquals(7, dest.get())
            scope2.close() // Kill the destination value's scope
            origin.set(8)
            changes.expectNone()
            assertEquals(7, dest.get()) // Because dest scope was killed it shouldn't receive any more updates
        }
    }

    @Test fun killOriginScope() {
        runThenCancel {
            val origin = scope1.watchableValueOf(5)
            val dest = scope2.watchableValueOf(6)
            scope2.bind(origin, dest)
            origin.set(7)
            scope2.watch(dest) { changes += it }
            changes.expect(ValueChange(7, 7))

            scope1.close() // Kill the origin value's scope
            assertFalse(origin.isActive)
            // Because origin scope was killed it should NOT pass further values on to dest
            origin.set(8)
            changes.expectNone()
            assertEquals(7, dest.get())
        }
    }
}
