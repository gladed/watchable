[io.gladed.watchable.store](../index.md) / [Hold](./index.md)

# Hold

`interface Hold`

Represents resources held on behalf of an object.

### Functions

| Name | Summary |
|---|---|
| [onCancel](on-cancel.md) | `abstract fun onCancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately cancel the hold. |
| [onCreate](on-create.md) | `abstract suspend fun onCreate(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Handle the initial creation of the underlying held object. |
| [onRemove](on-remove.md) | `abstract suspend fun onRemove(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Handle removal of the held object. |
| [onStart](on-start.md) | `abstract suspend fun onStart(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Start any internals with a delayed start. |
| [onStop](on-stop.md) | `abstract suspend fun onStop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called to gently stop any internal aspects of the hold. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `operator fun invoke(onStart: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = { }, onStop: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = { }, onCancel: () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = { }, onRemove: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = { }, onCreate: suspend () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = { }): `[`Hold`](./index.md)<br>Create a [Hold](./index.md) object with supplied implementations. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [plus](../plus.md) | `operator fun `[`Hold`](./index.md)`.plus(other: `[`Hold`](./index.md)`): `[`Hold`](./index.md)<br>Combine the behaviors of this [Hold](./index.md) object with [other](../plus.md#io.gladed.watchable.store$plus(io.gladed.watchable.store.Hold, io.gladed.watchable.store.Hold)/other).`operator fun `[`Hold`](./index.md)`.plus(other: `[`Watcher`](../../io.gladed.watchable/-watcher/index.md)`): `[`Hold`](./index.md)<br>Combine the behaviors of this [Hold](./index.md) object with a [Watcher](../../io.gladed.watchable/-watcher/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
