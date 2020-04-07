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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import model.Bird
import model.Chirp
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

@OptIn(UnstableDefault::class)
class AdapterTest {
    @Rule @JvmField
    val folder = TemporaryFolder()

    private val robin = Bird(name = "robin")
    private val chirp = Chirp(from = robin.id, text = "hi")

    @Test fun `read and write`() = runBlocking<Unit> {
        Adapter.createLogic(coroutineContext, folder.root).scoped(this).apply {
            birds.put(robin.id, robin)
        }
    }

    @Test fun `read and write between adapter instances`() {
        runBlocking {
            Adapter.createLogic(coroutineContext, folder.root).scoped(this).apply {
                birds.put(robin.id, robin)
            }
        }

        runBlocking {
            Adapter.createLogic(coroutineContext, folder.root).scoped(this).apply {
                assertEquals(robin, birds.get(robin.id))
            }
        }
    }

    @Test fun `show objects`() {
        println(Json.stringify(Bird.serializer(), robin))
        println(Json.stringify(Chirp.serializer(), chirp))
    }
}
