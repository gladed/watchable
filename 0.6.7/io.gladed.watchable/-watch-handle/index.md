[io.gladed.watchable](../index.md) / [WatchHandle](./index.md)

# WatchHandle

`interface WatchHandle`

Allows management over a watch operation.

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel the watch operation immediately so that no further changes are handled. |
| [close](close.md) | `abstract fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Close the watch operation, allowing all outstanding changes to be delivered first. |
| [closeAndJoin](close-and-join.md) | `open suspend fun closeAndJoin(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Close the subscription with [close](close.md) and wait for all events to be processed with [join](join.md). |
| [join](join.md) | `abstract suspend fun join(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Suspend until all outstanding changes are drained and the watch operation is completed. |
| [plus](plus.md) | `open operator fun plus(right: `[`WatchHandle`](./index.md)`): `[`WatchHandle`](./index.md)<br>Merge two [WatchHandle](./index.md) objects, returning a single one that spans both. |
