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

package external

import io.gladed.watchable.store.Store
import io.gladed.watchable.store.cannot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes

/** Read/write strings to files for each key. */
class FileStore(
    rootDir: File,
    private val name: String,
    suffix: String = "txt"
) : Store<String> {

    private val dir = File(rootDir, name)
    private val dotSuffix = if (suffix.isBlank()) "" else ".$suffix"

    private fun String.keyFile() = File(dir.also { dir.mkdirs() }, "$this$dotSuffix")

    override suspend fun get(key: String) =
        withContext(Dispatchers.IO) {
            key.keyFile().takeIf { it.isFile }?.readText()
                ?: cannot("find $name for key '$key'")
        }

    override suspend fun put(key: String, value: String) {
        withContext(Dispatchers.IO) {
            key.keyFile().writeText(value)
        }
    }

    override suspend fun remove(key: String) {
        withContext(Dispatchers.IO) {
            key.keyFile().delete()
        }
    }

    /** Keys of found files, in order from most recent to oldest. */
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    override fun keys() = flow<Flow<File>> {
        emit((dir.listFiles() ?: arrayOf()).sortedBy {
            Files.readAttributes(it.toPath(), BasicFileAttributes::class.java).creationTime()
        }.reversed().asFlow())
    }.flowOn(Dispatchers.IO).flattenConcat().mapNotNull {
        if (it.name.endsWith(dotSuffix)) {
            it.name.removeSuffix(dotSuffix)
        } else null
    }
}
