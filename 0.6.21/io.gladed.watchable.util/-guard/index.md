[io.gladed.watchable.util](../index.md) / [Guard](./index.md)

# Guard

`interface Guard<T>`

Provides mutually-exclusive access to a value of [T](index.md#T).

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `abstract suspend operator fun <U> invoke(func: suspend `[`T`](index.md#T)`.() -> `[`U`](invoke.md#U)`): `[`U`](invoke.md#U)<br>Allows access to [T](index.md#T) while a [Mutex](#) is held. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../guard.md) | `fun <T> `[`T`](../guard.md#T)`.guard(): `[`Guard`](./index.md)`<`[`T`](../guard.md#T)`>`<br>Return [T](../guard.md#T) surrounded by a [Guard](./index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
