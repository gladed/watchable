[io.gladed.watchable](../index.md) / [ValueChange](./index.md)

# ValueChange

`data class ValueChange<T> : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>`

Describes the replacement of a value.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ValueChange(oldValue: `[`T`](index.md#T)`, newValue: `[`T`](index.md#T)`)`<br>Describes the replacement of a value. |

### Properties

| Name | Summary |
|---|---|
| [newValue](new-value.md) | `val newValue: `[`T`](index.md#T)<br>The new object value. |
| [oldValue](old-value.md) | `val oldValue: `[`T`](index.md#T)<br>The old value, or the same as [newValue](new-value.md) if this is the initial notification. |
