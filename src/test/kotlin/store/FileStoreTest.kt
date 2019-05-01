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

package store

import impossible
import io.gladed.watchable.store.FileStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@UseExperimental(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FileStoreTest {
    @Rule @JvmField val folder = TemporaryFolder()

    private lateinit var store: FileStore

    @Before fun setup() {
        folder.root.mkdirs()
        store = FileStore(folder.root, "thing")
    }

    @Test fun `put an object`() = runBlocking {
        store.put("^1", "1")
        assertThat(File(folder.root, "thing").listFiles().map { it.name }, hasItem("^1.txt"))
    }

    @Test fun `get an object`() = runBlocking {
        store.put("^1", "1")
        assertEquals("1", store.get("^1"))
    }

    @Test fun `overwrite an object`() = runBlocking {
        store.put("^1", "1")
        store.put("^1", "2")
        assertEquals("2", store.get("^1"))
    }

    @Test fun `cannot get a non-existing object`() = runBlocking {
        impossible {
            store.get("^1")
        }
    }

    @Test fun `delete an object`() = runBlocking {
        store.put("^1", "1")
        store.delete("^1")
        impossible { store.get("^1") }
    }

    @Test fun `keys returns list of available objects`() = runBlocking {
        store.put("^1", "1")
        store.put("^2", "1")
        // Order is unimportant
        assertEquals(setOf("^1", "^2"), store.keys().toSet())
    }
}
