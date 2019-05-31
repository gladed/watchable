[io.gladed.watchable](../index.md) / [ValueChange](./index.md)

# ValueChange

`data class ValueChange<T> : `[`Change`](../-change/index.md)

Describes the update of a value.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ValueChange(oldValue: `[`T`](index.md#T)`? = null, value: `[`T`](index.md#T)`, isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`<br>Describes the update of a value. |

### Properties

| Name | Summary |
|---|---|
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
| [oldValue](old-value.md) | `val oldValue: `[`T`](index.md#T)`?`<br>The old value, if any. |
| [value](value.md) | `val value: `[`T`](index.md#T)<br>The new value. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
