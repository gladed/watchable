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
            }.func()}
    }
}

@UseExperimental(ObsoleteCoroutinesApi::class)
suspend fun <C> ReceiveChannel<C>.assert(vararg items: C) {
    if (items.isEmpty()) {
        assertEquals(null, poll())
    } else {
        for (item in items) {
            val rx = withTimeoutOrNull(150) { receiveOrNull() }
            log("Rx: ${rx ?: "timeout"}")
            assertEquals(item, rx)
        }
    }
}
