import io.gladed.watchable.TestContextWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.withTestContext
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals

/** A scope in which tests may run. */
@UseExperimental(ObsoleteCoroutinesApi::class)
interface TestCoroutineScope : CoroutineScope {
    val testContext: TestCoroutineContext

    fun advanceTimeBy(amount: Int) {
        testContext.advanceTimeBy(amount.toLong())
    }

    fun triggerActions() {
        testContext.triggerActions()
    }

    fun newScope() = CoroutineScope(coroutineContext + Job())
}

/** Run a test within a [TestCoroutineScope]. */
@UseExperimental(ObsoleteCoroutinesApi::class)
fun runTest(func: suspend TestCoroutineScope.() -> Unit) {
    withTestContext {
        runBlocking(this) {
            object : TestCoroutineScope {
                override val testContext = this@withTestContext
                override val coroutineContext = this@runBlocking.coroutineContext + TestContextWrapper(testContext)
            }.func()
        }
    }
}

@UseExperimental(ObsoleteCoroutinesApi::class)
suspend fun <C> ReceiveChannel<C>.assert(vararg items: C) {
    if (items.isEmpty()) {
        assertEquals(null, poll())
    } else {
        for (item in items) {
            val rx = withTimeoutOrNull(150) { receiveOrNull() }
            log("Rx: $rx")
            assertEquals(item, rx)
        }
    }
}
