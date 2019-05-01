[io.gladed.watchable](../index.md) / [Watcher](./index.md)

# Watcher

`interface Watcher`

An ongoing watch operation.

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `abstract fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately stop. Repeated invocations have no effect. |
| [plus](plus.md) | `open operator fun plus(other: `[`Watcher`](./index.md)`): `[`Watcher`](./index.md)<br>Combine two [Watcher](./index.md) objects, returning a single one that spans both. |
| [start](start.md) | `abstract suspend fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Return when the watcher has become effective. |
| [stop](stop.md) | `abstract suspend fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Gracefully stop, suspending if necessary to allow outstanding operations to complete. Repeated invocations have no effect. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
