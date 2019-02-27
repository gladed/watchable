[io.gladed.watchable](../index.md) / [ValueChange](./index.md)

# ValueChange

`data class ValueChange<T> : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>`

An initial announcement or change to the underlying value for a [WatchableValue](../-watchable-value/index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ValueChange(newValue: `[`T`](index.md#T)`, oldValue: `[`T`](index.md#T)`)`<br>An initial announcement or change to the underlying value for a [WatchableValue](../-watchable-value/index.md). |

### Properties

| Name | Summary |
|---|---|
| [newValue](new-value.md) | `val newValue: `[`T`](index.md#T)<br>The new object value. |
| [oldValue](old-value.md) | `val oldValue: `[`T`](index.md#T)<br>The old value, or the same as [newValue](new-value.md) if this is the initial notification. |
