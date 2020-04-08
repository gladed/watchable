[io.gladed.watchable](../index.md) / [Value](./index.md)

# Value

`interface Value<T>`

A read-only wrapper for a value of [T](index.md#T).

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T) |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [MutableValue](../-mutable-value/index.md) | `interface MutableValue<T> : `[`Value`](./index.md)`<`[`T`](../-mutable-value/index.md#T)`>`<br>A container for a value that can be changed. |
