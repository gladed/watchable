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

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executors

class ChangeWatcherRule<C> : TestRule, CoroutineScope {

    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "change-watcher")
    }.asCoroutineDispatcher()

    override val coroutineContext = dispatcher + Job() + CoroutineExceptionHandler { _, cause -> log(cause.toString())}

    private val changes = Channel<C>(Channel.UNLIMITED)
    private val changeLists = Channel<List<C>>(Channel.UNLIMITED)

    operator fun plusAssign(change: C) {
        log("Change: $change")
        launch {
            changes.send(change)
            changeLists.send(listOf(change))

        }
    }

    operator fun plusAssign(changeList: List<C>) {
        log("Changes: $changeList")
        launch {
            changeList.forEach { changes.send(it) }
            changeLists.send(changeList)
        }
    }

    suspend fun expect(vararg expected: C, timeout: Long = 250, strict: Boolean = true) {
        val expectedList = expected.toList()
        val current = mutableListOf<C>()

        val result = withTimeoutOrNull(timeout) {
            while (expectedList != current) {
                if (strict && current.isNotEmpty()) {
                    assertEquals(expectedList.take(current.size), current)
                }
                current.add(changes.receive())
                if (!strict && current.size > expectedList.size) {
                    current.removeAt(0)
                }
            }
        }
        if (result == null) {
            assertEquals(expectedList, current)
        }
    }

    suspend fun expectNone() {
        delay(25)
        assertEquals(null, changes.poll())
    }

    fun mustHaveLists(vararg mustChangeLists: List<C>) {
        for (mustChangeList in mustChangeLists) {
            assertEquals(mustChangeList, changeLists.poll())
        }
        assertEquals(null, changeLists.poll())
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            try {
                base.evaluate()
            } finally {
                coroutineContext[Job]!!.cancel()
            }
        }
    }
}
