package test

import io.gladed.watchable.TestContextWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.withTestContext
import org.junit.Assert
import store.Cannot

/** Run a test within a [TestCoroutineScope]. */
@UseExperimental(ObsoleteCoroutinesApi::class)
fun runTest(func: suspend TestCoroutineScope.() -> Unit) {
    withTestContext {
        runBlocking(this) {
            object : TestCoroutineScope {
                override val testContext = this@withTestContext
                override val coroutineContext = this@runBlocking.coroutineContext + TestContextWrapper(testContext)
            }.func()}
    }
}

suspend fun impossible(func: suspend () -> Unit) {
    try {
        func()
        Assert.fail("should have failed")
    } catch (c: Cannot) {
        println("As expected: $c")
    }
}

/** A scope in which tests may run. */
@UseExperimental(ObsoleteCoroutinesApi::class)
interface TestCoroutineScope : CoroutineScope {
    val testContext: TestCoroutineContext

    fun advanceTimeBy(amount: Int) {
        testContext.advanceTimeBy(amount.toLong())
    }

    fun trigger() {
        testContext.triggerActions()
    }

    fun newScope() = CoroutineScope(coroutineContext + Job())

    /** Launch a new coroutine scope and execute [func] within it, afterwards triggering any leftover actions. */
    suspend fun <U> inScope(func: suspend CoroutineScope.() -> U): U =
        coroutineScope {
            func()
        }.also { trigger() }
}

