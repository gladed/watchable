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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.lang.ref.WeakReference

class MemoryLeakTest {
    private val scope1 = CoroutineScope(Dispatchers.Default)
    private val scope2 = CoroutineScope(Dispatchers.Default)
    @Rule @JvmField val changes = ChangeWatcherRule<ListChange<Int>>()

    @Test fun noScopeLeak() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = scope1.watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            // Watch it from scope 2
            scope2.watch(list1) {
                changes += it
            }

            // Cancel both scopes and drop the var
            scope1.coroutineContext[Job]?.cancel()
            scope2.coroutineContext[Job]?.cancel()
            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }

    @Test fun noJobLeak() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = scope1.watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            // Watch it from scope 2
            val job = scope2.watch(list1) {
                changes += it
            }

            // Cancel scope1 and the job (leave scope2 running)
            scope1.coroutineContext[Job]?.cancel()
            job.cancel()
            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }

    @Test fun noBindLeak() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = scope1.watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            scope2.launch {
                val list2 = watchableListOf(4)
                list2.bind(list1!!)
            }.join()

            // Kill both scopes, and make sure list1 can be freed
            scope1.coroutineContext[Job]?.cancel()
            scope2.coroutineContext[Job]?.cancel()
            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }

    @Test fun noLostBindLeak() {
        runBlocking {
            // Create a var in scope1
            var list1: WatchableList<Int>? = scope1.watchableListOf(1, 2, 3)
            val ref = WeakReference(list1!!)

            scope2.launch {
                val list2 = watchableListOf(4)
                list2.bind(list1!!)
                delay(50)
            }.join()

            // Kill scope1 only and drop ref to list1. Scope2 is still bound and listening but its origin has vanished.
            scope1.coroutineContext[Job]?.cancel()
            list1 = null
            assertNull(list1)

            scour { assertNull(ref.get()) }
        }
    }

    private fun scour(maxIterations: Int = 50, delay: Int = 50, untilSuccess: () -> Unit) {
        val runtime = Runtime.getRuntime()
        for (i in 0 until maxIterations) {
            runtime.runFinalization()
            runtime.gc()
            try {
                untilSuccess()
                return
            } catch (e: AssertionError) { } // Keep trying
            Thread.sleep(delay.toLong())
        }
        untilSuccess()
    }
}
