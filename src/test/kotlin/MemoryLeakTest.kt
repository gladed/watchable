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

import io.gladed.watchable.ListChange
import io.gladed.watchable.WatchableList
import io.gladed.watchable.bind
import io.gladed.watchable.subscribe
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.lang.ref.WeakReference

@Suppress("UNUSED_VALUE") // We mean to release items when we are done with them.
class MemoryLeakTest {
    private val scope1 = CoroutineScope(Dispatchers.Default)
    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test fun `cancel of watch scope allows gc`() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            // Watch it from scope 2
            scope1.watch(list1) {
                changes += it
            }

            // Cancel both scopes and drop the var
            scope1.coroutineContext[Job]?.cancel()
            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }

    @Test fun `cancel of subscription allows gc`() {
        runBlocking {
            var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)
            var sub: ReceiveChannel<List<ListChange<Int>>>? = scope1.subscribe(list1)
            // Cancel the sub and drop vars
            sub?.cancel()
            sub = null
            list1 = null

            scour { assertNull(ref.get()) }
        }
    }

    @Test fun `cancel of nothing allows gc`() {
        runBlocking {
            var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)
            list1 = null
            scour {
                assertNull(ref.get())
            }
        }
    }

    @Test fun `cancel of watch job allows gc`() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            // Watch it from the current scope
            var job: Job? = watch(list1) {
            }

            // Cancel and forget the job but leave the scope running.
            job?.cancel()
            log(job)
            job = null
            list1 = null

            log("Scouring now, $coroutineContext")
            delay(50)
            scour { assertNull(ref.get()) }
        }
    }

    @Test fun `join on scope used to bind allows gc`() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            scope1.launch {
                val list2 = watchableListOf(4)
                bind(list2, list1!!)
            }.join() // join returns when scope is complete (and therefore bind should be dead)

            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }
}
