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

import io.gladed.watchable.Change
import io.gladed.watchable.Period.INLINE
import io.gladed.watchable.WatchableBase
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.yield
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

@UseExperimental(ObsoleteCoroutinesApi::class)
class OnDemandTest {

    interface Exchange

    data class Tx(val data: String): Exchange {
        constructor(data: Int) : this(data.toString())
    }

    data class Rx(val data: List<String>): Exchange {
        constructor(vararg data: Any) : this(data.map { it.toString() })
        constructor(data: String) : this(listOf(data))
    }

    private val tape = Channel<Exchange>(UNLIMITED)

    data class MyChange(val value: String) : Change {
        override fun toString() = value
    }

    class MyWatchableBase : WatchableBase<MyChange>() {
        override fun getInitialChange() = MyChange("hi")
        suspend fun change(value: Any) {
            dispatch(listOf(MyChange(value.toString())))
        }
    }

    private val watchable = MyWatchableBase()

    private suspend fun change(value: Int) {
        tape.send(Tx(value.toString()))
        watchable.change(MyChange(value.toString()))
    }

    private suspend fun delivered(changes: List<MyChange>) {
        tape.send(Rx(changes.map { it.toString() }))
    }

    private suspend fun receive(vararg expected: Any) {
        if (expected.isNotEmpty()) {
            for (item in expected) {
                assertEquals(item.toString(), tape.receive().toString())
            }
        } else {
            yield()
            assertTrue("no events expected", tape.poll() == null)
        }
    }

    @Test(timeout = 500) fun `receive only new items`() = runTest {
        change(1)
        watchable.batch(this, 0) { delivered(it) }
        change(2)
        change(3)

        receive(Tx(1), Tx(2), Tx(3), Rx("hi"), Rx(2), Rx(3))
    }

    @Test(timeout = 500) fun `receive items inline`() = runTest {
        watchable.batch(this, INLINE) { delivered(it) }
        change(1)
        change(2)

        receive(Rx("hi"), Tx(1), Rx(1), Tx(2), Rx(2))
    }

    @Test(timeout = 500) fun `throw inline`() = runTest {
        watchable.batch(this, INLINE) {
            if (it.contains(MyChange("2"))) fail("do not like")
            delivered(it)
        }
        change(1)
        try {
            change(2)
            fail("Should have thrown")
        } catch (failure: AssertionError) {  }
    }

    @Test(timeout = 500) fun `continue after inline throw`() = runTest {
        watchable.batch(this, INLINE) {
            if (it.contains(MyChange("2"))) fail("do not like")
            delivered(it)
        }
        change(1)

        try {
            change(2)
            fail("Should throw")
        } catch (failure: AssertionError) {  }

        change(3)
    }

    @Test(timeout = 500) fun `receive items delayed`() = runTest {
        watchable.batch(this, 100) { delivered(it) }
        change(1)
        change(2)

        receive(Tx(1))
        receive(Tx(2))
        advanceTimeBy(50)
        receive()
        advanceTimeBy(50)
        receive(Rx("hi", 1, 2))
    }

    @Test(timeout = 500) fun `close batch handle allows old events to arrive`() = runTest {
        change(1)
        val handle = watchable.batch(this, 100) { delivered(it) }
        change(2)
        handle.stop()
        change(3)

        receive(Tx(1), Tx(2), Rx("hi", 2), Tx(3))
        receive()
    }

    @Test(timeout = 500) fun `cancel batch handle kills old events`() = runTest {
        change(1)
        val handle = watchable.batch(this, 100) { delivered(it) }
        change(2)
        handle.cancel()
        change(3)

        receive(Tx(1), Tx(2), Tx(3))
        receive()
    }

    @Test(timeout = 500) fun `close is safe after cancel`() = runTest {
        val handle = watchable.batch(this, 100) { delivered(it) }
        handle.cancel()
        handle.stop()
    }

    @Test(timeout = 500) fun `open and close`() = runTest {
        val handle = watchable.batch(this) { delivered(it) }
        change(1)
        handle.stop()
        receive(Tx(1), Rx("hi"), Rx(1))
    }
}
