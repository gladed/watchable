package store

import java.io.File

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
        key.keyFile().takeIf { dir.exists() }?.readText()
            ?: cannot("find $name for key")

    override suspend fun put(key: String, value: String) {
        key.keyFile().writeText(value)
    }

    override suspend fun delete(key: String) {
        key.keyFile().delete()
    }
}
