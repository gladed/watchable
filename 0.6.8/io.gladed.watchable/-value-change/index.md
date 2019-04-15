[io.gladed.watchable](../index.md) / [ValueChange](./index.md)

# ValueChange

`data class ValueChange<T> : `[`Change`](../-change.md)

Describes the update of a value.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ValueChange(oldValue: `[`T`](index.md#T)`? = null, value: `[`T`](index.md#T)`)`<br>Describes the update of a value. |

### Properties

| Name | Summary |
|---|---|
| [oldValue](old-value.md) | `val oldValue: `[`T`](index.md#T)`?`<br>The old value, if any. |
| [value](value.md) | `val value: `[`T`](index.md#T)<br>The new value. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
