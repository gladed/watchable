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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.fail
import io.gladed.watchable.util.Cannot

suspend fun impossible(func: suspend () -> Unit) {
    try {
        func()
        fail("should have failed")
    } catch (c: Cannot) {
        println("As expected: $c")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun runTest(func: suspend TestCoroutineScope.() -> Unit) {
    TestCoroutineScope(TestCoroutineDispatcher() + Job()).runBlockingTest {
        func()
    }
}
