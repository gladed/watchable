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
import io.gladed.watchable.toWatchableList
import io.gladed.watchable.waitFor
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class BindValueTest {
    val changes = Channel<Any>(Channel.UNLIMITED)

    @Test fun `bind value`() = runTest {
        val origin = watchableValueOf(5)
        val dest = watchableValueOf(6)
        bind(dest, origin)
        eventually { assertEquals(5, dest.value) }
    }

    @Test fun `example from readme`() = runTest {
        val from = listOf(4, 5).toWatchableList()
        val into = watchableListOf<Int>()
        bind(into, from)
        assertEquals(from, into)
    }

    @Test fun `bind then change`() = runTest {
        val origin = watchableValueOf(5)
        val dest = watchableValueOf(6)
        watch(dest) { changes.send(it) }
        changes.mustBe(ValueChange(null, 6, true))

        bind(dest, origin)
        origin.set(7)

        changes.mustBe(ValueChange(6, 5), ValueChange(5, 7))
        assertEquals(7, dest.value)
    }

    @Test fun `bind to self`() {
        try {
            runTest {
                val origin = watchableValueOf(5)
                bind(origin, origin)
                fail("Shouldn't get here")
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun `bind twice`() {
        try {
            runTest {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                bind(dest, origin)
                bind(dest, origin)
                fail("Second bind should have thrown")
            }
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test fun `circular binding`() {
        try {
            runTest {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                bind(dest, origin)
                bind(origin, dest)
                fail("Second bind should have thrown")
            }
        } catch (e: IllegalStateException) {
            // expected
        }
    }

    @Test fun `modify bound`() {
        try {
            runTest {
                val origin = watchableValueOf(5)
                val dest = watchableValueOf(6)
                bind(dest, origin)
                dest.set(7)
                fail("Modification should not be permitted")
                assertEquals(6, dest.value)
            }
        } catch (e: IllegalStateException) {
            // Expected
        }
    }

    @Test fun `bind then unbind`() = runTest {
        val origin = watchableValueOf(5)
        val dest = watchableValueOf(6)
        bind(dest, origin)
        waitFor(dest) { 5 == dest.value }
        dest.unbind()
        origin.set(7)
        assertEquals(5, dest.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `watch from different scope`() = runTest {
        val origin = watchableValueOf(5)
        val scope1 = CoroutineScope(coroutineContext)
        scope1.watch(origin) {
            changes.send(it)
        }
        log("Asserting after $scope1 launch")
        changes.mustBe(ValueChange(null, 5, true))
        origin.set(6)
        changes.mustBe(ValueChange(5, 6))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test fun `kill binding scope`() = runTest {
        val origin = watchableValueOf(5)
        val dest = watchableValueOf(6)
        watch(dest) { changes.send(it) }
        val scope1 = CoroutineScope(coroutineContext + SupervisorJob())
        scope1.bind(dest, origin)
        origin.set(7)
        assertEquals(7, dest.value)

        // Kill the destination value's scope, which should also kill the binding
        scope1.coroutineContext.cancel()

        // Changing origin has no effect on bound thing, though it is still bound
        origin.set(8)
        assertEquals(7, dest.value)
        assertTrue(dest.isBound())
    }
}
