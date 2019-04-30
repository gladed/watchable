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

import external.Adapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import model.Bird
import model.MutableBird
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@UseExperimental(ExperimentalCoroutinesApi::class)
class AdapterTest {
    @Rule @JvmField val folder = TemporaryFolder()
    private val robin = Bird(name = "robin")

    @Test fun `read and write`() = runBlocking {
        val adapter = Adapter.createLogic(coroutineContext, folder.root)
        val birds = adapter.birds.create(this)
        birds.put(robin.id, MutableBird.inflate(robin))
    }

    @Test fun `read and write between adapter instances`() {
        runBlocking {
            val adapter = Adapter.createLogic(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            birds.put(robin.id, MutableBird.inflate(robin))
        }

        runBlocking {
            val adapter = Adapter.createLogic(coroutineContext, folder.root)
            val birds = adapter.birds.create(this)
            assertEquals(robin, MutableBird.deflate(birds.get(robin.id)))
        }
    }
}
