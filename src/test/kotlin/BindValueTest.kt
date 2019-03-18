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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test

@UseExperimental(ObsoleteCoroutinesApi::class)
class BindValueTest : ScopeTest() {
    @Rule @JvmField val changes = ChangeWatcherRule<ValueChange<Int>>()

    @Test fun `bind value`() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            bind(dest, origin)
            eventually { assertEquals(5, dest.value) }
        }
    }

    @Test fun `example from readme`() {
        val from = listOf(4, 5).toWatchableList()
        val into = watchableListOf<Int>()
        bind(into, from)
        runBlocking {
            eventually { assertEquals(from, into) }
        }
    }

    @Test fun `bind then change`() {
        runBlocking {

            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            watch(dest) { changes += it }
            changes.expect(ValueChange(6, 6))
            bind(dest, origin)
            origin.set(7)
            // Was dest ever 5?
            changes.expect(ValueChange(6, 7))
            assertEquals(7, dest.value)
        }
    }

    @Test fun `bind to self`() {
        try {
            runBlocking {
                val origin = watchableValueOf(5)
                bind(origin, origin)
                fail("Shouldn't get here")
            }
        } catch (e: IllegalStateException) { }
    }

    @Test fun `bind twice`() {
        try {
            runBlocking {
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
            runBlocking {
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
            runBlocking {
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

    @Test fun `bind then unbind`() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            bind(dest, origin)
            eventually { assertEquals(5, dest.value) }
            dest.unbind()
            origin.set(7)
            always { assertEquals(5, dest.value) }
        }
    }

    private val scope1 = LocalScope(newSingleThreadContext("scope1"))

    @Test fun `watch from different scope`() {
        runBlocking {
            val origin = watchableValueOf(5)
            scope1.watch(origin) {
                log(it)
                assertThat(Thread.currentThread().name, containsString("scope1"))
                changes += it
            }
            changes.expect(ValueChange(5, 5))
            origin.set(6)
            changes.expect(ValueChange(5, 6))
        }
    }

    @Test fun `kill binding scope`() {
        runBlocking {
            val origin = watchableValueOf(5)
            val dest = watchableValueOf(6)
            watch(dest) { changes += it }
            scope1.bind(dest, origin)
            origin.set(7)
            eventually { assertEquals(7, dest.value) }
            scope1.close() // Kill the destination value's scope

            // Changing origin has no effect on bound thing, though it is still bound
            origin.set(8)
            always { assertEquals(7, dest.value) }
            assertTrue(dest.isBound())
        }
    }
}
