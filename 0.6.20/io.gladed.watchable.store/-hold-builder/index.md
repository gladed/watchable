[io.gladed.watchable.store](../index.md) / [HoldBuilder](./index.md)

# HoldBuilder

`class HoldBuilder`

DSL for creating a [Hold](../-hold/index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HoldBuilder()`<br>DSL for creating a [Hold](../-hold/index.md). |

### Functions

| Name | Summary |
|---|---|
| [build](build.md) | `fun build(): `[`Hold`](../-hold/index.md)<br>Build the [Hold](../-hold/index.md) from the current builder state. |
| [onCancel](on-cancel.md) | `fun onCancel(func: () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancels the hold immediately with no further handling. |
| [onCreate](on-create.md) | `fun onCreate(func: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Provide a function to be invoked when an object is being created. |
| [onRemove](on-remove.md) | `fun onRemove(func: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Invoke when the held object is to be removed. |
| [onStart](on-start.md) | `fun onStart(func: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Starts operations related to the hold. |
| [onStop](on-stop.md) | `fun onStop(func: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Invoke when the hold is to be stopped. |
| [onWatcher](on-watcher.md) | `fun onWatcher(watcher: `[`Watcher`](../../io.gladed.watchable/-watcher/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adopt a [Watcher](../../io.gladed.watchable/-watcher/index.md) as part of the [Hold](../-hold/index.md) to be built, cancelling, stopping, and starting it together. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
