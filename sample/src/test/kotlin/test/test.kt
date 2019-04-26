package test

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail
import io.gladed.watchable.store.Cannot

suspend fun impossible(func: suspend () -> Unit) {
    try {
        func()
        fail("should have failed")
    } catch (c: Cannot) {
        println("As expected: $c")
    }
}

@UseExperimental(ExperimentalCoroutinesApi::class)
fun runTest(func: suspend TestCoroutineScope.() -> Unit) {
    TestCoroutineScope(TestCoroutineDispatcher() + Job()).runBlockingTest {
        func()
    }
}
