package store

import java.io.File

/** Read/write strings to files for each key. */
class FileStore(
    private val root: File,
    suffix: String = "txt"
) : Store<String> {

    private val dotSuffix = if (suffix.isBlank()) "" else ".$suffix"
    private fun String.keyFile() = File(root.also { it.mkdirs() }, "$this$dotSuffix")

    override suspend fun get(key: String) =
        key.keyFile().takeIf { it.exists() }?.readText()
            ?: cannot("no file for key")

    override suspend fun put(key: String, value: String) {
        key.keyFile().writeText(value)
    }

    override suspend fun delete(key: String) {
        key.keyFile().delete()
    }
}
