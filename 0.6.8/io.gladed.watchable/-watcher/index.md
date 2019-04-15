[io.gladed.watchable](../index.md) / [Watcher](./index.md)

# Watcher

`interface Watcher`

An ongoing watch operation that can be closed or cancelled.

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately stop. Repeated invocations have no effect. |
| [close](close.md) | `abstract suspend fun close(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Gracefully stop, suspending if necessary to allow underlying operations to complete. Repeated invocations have no effect. |
| [plus](plus.md) | `open operator fun plus(right: `[`Watcher`](./index.md)`): `[`Watcher`](./index.md)<br>Combine two [Watcher](./index.md) objects, returning a single one that spans both. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
