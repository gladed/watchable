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
import io.gladed.watchable.watch
import io.gladed.watchable.watchableListOf
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.junit.Assert.assertNull
import org.junit.Test
import java.lang.ref.WeakReference

@Suppress("UNUSED_VALUE") // We mean to release items when we are done with them.
class MemoryLeakTest {
    val changes = Channel<ListChange<Int>>(Channel.UNLIMITED)

    @Test fun `gc is detectable`() = runTest {
        var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
        val ref = WeakReference(list1!!)
        list1 = null
        scour { assertNull(ref.get()) }
    }

    @Test fun `cancel of watch scope allows gc`() = runTest {
        val scope1 = newScope()

        // Create a var in scope1
        var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
        val ref = WeakReference(list1!!)

        // Watch it from scope 2
        scope1.watch(list1) { changes.send(it) }

        // Cancel both scopes and drop the var
        scope1.coroutineContext[Job]?.cancel()
        list1 = null
        assertNull(list1)

        scour { assertNull(ref.get()) }
    }

    @Test fun `cancel of handle allows gc`() = runTest {
        val scope1 = newScope()

        var list1: WatchableList<Int>? = watchableListOf(1, 2, 3)
        val ref = WeakReference(list1!!)
        // Watch and then cancel
        scope1.watch(list1) { changes.send(it) }.cancel()
        list1 = null

        // Detect gc of list1
        scour { assertNull(ref.get()) }
    }

    @Test fun `join on scope used to bind allows gc`() = runTest {
        val scope1 = newScope()

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
