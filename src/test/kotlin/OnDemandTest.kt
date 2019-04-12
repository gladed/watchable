import kotlinx.coroutines.ObsoleteCoroutinesApi

@UseExperimental(ObsoleteCoroutinesApi::class)
class OnDemandTest {

//    data class MyChange(val value: String) : Change {
//        override fun toString() = value
//    }
//
//    private val watchable = object : WatchableBase<MyChange>() {
//        override fun getInitialChange() = MyChange("hi")
//    }
//
//    private val tape = Channel<String>(UNLIMITED)
//
//    private suspend fun change(value: Any) {
//        tape.send("$value")
//        watchable.change(listOf(MyChange(value.toString())))
//    }
//
//    @UseExperimental(ExperimentalCoroutinesApi::class)
//    private suspend fun receive(vararg expected: Any) {
//        if (expected.isNotEmpty()) {
//            for (item in expected) {
//                assertEquals(item.toString(), tape.receive())
//            }
//        } else {
//            yield()
//            assertTrue("no events expected", tape.isEmpty)
//        }
//    }
//
//
//    @Test(timeout = 500) fun `receive only new items`() = test {
//        change(1)
//        watchable.batch(this, 0) { tape.send(it.toString()) }
//        change(2)
//        change(3)
//
//        receive(1, 2, 3, listOf("hi"), listOf(2), listOf(3))
//    }
//
//    @Test(timeout = 500) fun `receive items inline`() = test {
//        watchable.batch(this, INLINE) { tape.send(it.toString()) }
//        change(1)
//        change(2)
//
//        receive(listOf("hi"), 1, listOf(1), 2, listOf(2))
//    }
//
//    @Test(timeout = 500) fun `throw inline`() = test {
//        watchable.batch(this, INLINE) {
//            if (it.contains(MyChange("2"))) fail("do not like")
//            tape.send(it.toString())
//        }
//        change(1)
//        try {
//            change(2)
//            fail("Should have thrown")
//        } catch (failure: AssertionError) {  }
//    }
//
//    @Test(timeout = 500) fun `continue after inline throw`() = test {
//        watchable.batch(this, INLINE) {
//            if (it.contains(MyChange("2"))) fail("do not like")
//            tape.send(it.toString())
//        }
//        change(1)
//
//        try {
//            change(2)
//            fail("Should throw")
//        } catch (failure: AssertionError) {  }
//
//        change(3)
//    }
//
//    @Ignore // How to properly detect exception in launch'ed code?
//    @Test(timeout = 500) fun `ignore throw`() = test {
//        watchable.batch(this) {
//            if (it.contains(MyChange("2"))) {
//                fail("do not like")
//            }
//            tape.send(it.toString())
//        }
//        change(1)
//        change(2)
//        change(3)
//        receive(1, 2, 3, listOf("hi"), listOf(1))
//        receive()
//    }
//
//    @Test(timeout = 500) fun `receive items delayed`() = test {
//        watchable.batch(this, 100) { tape.send(it.toString()) }
//        change(1)
//        change(2)
//
//        receive(1)
//        receive(2)
//        runTest.advanceTimeBy(50)
//        receive()
//        runTest.advanceTimeBy(50)
//        receive(listOf("hi", 1, 2))
//    }
//
//    @Test(timeout = 500) fun `close batch handle allows old events to arrive`() = test {
//        change(1)
//        val handle = watchable.batch(this, 100) { tape.send(it.toString()) }
//        change(2)
//        handle.close()
//        change(3)
//
//        receive(1, 2, listOf("hi", 2), 3)
//        receive()
//    }
//
//    @Test(timeout = 500) fun `cancel batch handle kills old events`() = test {
//        change(1)
//        val handle = watchable.batch(this, 100) { tape.send(it.toString()) }
//        change(2)
//        handle.cancel()
//        change(3)
//
//        receive(1, 2, 3)
//        receive()
//    }
//
//    @Test(timeout = 500) fun `close is safe after cancel`() = test {
//        val handle = watchable.batch(this, 100) { tape.send(it.toString()) }
//        handle.cancel()
//        handle.close()
//    }
//
//    @Test(timeout = 500) fun `open and close`() = test {
//        val handle1 = watchable.batch(this) { tape.send(it.toString()) }
//        change(1)
//        handle1.close()
//        receive(1, listOf("hi"), listOf(1))
//    }
}
