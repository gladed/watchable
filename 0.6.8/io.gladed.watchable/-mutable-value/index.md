[io.gladed.watchable](../index.md) / [MutableValue](./index.md)

# MutableValue

`interface MutableValue<T> : `[`Value`](../-value/index.md)`<`[`T`](index.md#T)`>`

A container for a value that can be changed.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract var value: `[`T`](index.md#T) |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
