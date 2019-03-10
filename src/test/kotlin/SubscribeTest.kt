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

import io.gladed.watchable.SetChange
import io.gladed.watchable.ValueChange
import io.gladed.watchable.WatchableSet
import io.gladed.watchable.WatchableValue
import io.gladed.watchable.watch
import io.gladed.watchable.watchableSetOf
import io.gladed.watchable.watchableValueOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executors

class SubscribeTest : ScopeTest() {
    private val set = watchableSetOf(1)
    @Rule @JvmField val scope2 = ScopeRule(Dispatchers.Default)

    @Test fun sub() {
        val rxChanges = set.subscribe()
        runBlocking {
            assertEquals(listOf(SetChange.Initial(setOf(1))), rxChanges.receive())
            set.use { remove(1) }
            assertEquals(listOf(SetChange.Remove(1)), rxChanges.receive())
        }
    }

    @Test fun cancelSub() {
        val rxChanges = set.subscribe()
        runBlocking {
            assertEquals(listOf(SetChange.Initial(setOf(1))), rxChanges.receive())
            rxChanges.cancel()
            set.use { remove(1) }
            try {
                assertEquals(listOf(SetChange.Remove(1)), rxChanges.receive())
            } catch (e: ClosedReceiveChannelException) { }
        }
    }

    @Test fun cancelScope() {
        val set2 = scope2.watchableSetOf(2)
        val rxChanges = set2.subscribe()
        runBlocking {
            assertEquals(listOf(SetChange.Initial(setOf(2))), rxChanges.receive())

            // Cancel set2's scope
            scope2.coroutineContext[Job]?.cancel()

            set2.use { remove(2) }
            try {
                assertEquals(listOf(SetChange.Remove(1)), rxChanges.receive())
            } catch (e: ClosedReceiveChannelException) { }
        }
    }

    @Test fun batchScope() {
        val set2 = scope2.watchableSetOf(2)
        val rxChanges = set2.subscribe()
        runBlocking {
            val batchChannel = batch(rxChanges, 50)
            assertEquals(listOf(SetChange.Initial(setOf(2))), batchChannel.receive())
            val start = System.currentTimeMillis()

            set2.use { remove(2) }
            set2.use { add(3); add(4) }

            assertEquals(listOf(
                SetChange.Remove(2),
                SetChange.Add(3),
                SetChange.Add(4)), batchChannel.receive())
            assertTrue(50 <= System.currentTimeMillis() - start)

            // Cancel set2's scope, immediately killing batchChannel
            scope2.coroutineContext[Job]?.cancel()
            try {
                assertEquals(listOf(SetChange.Remove(1)), batchChannel.receive())
            } catch (e: ClosedReceiveChannelException) { }
        }
    }
}
