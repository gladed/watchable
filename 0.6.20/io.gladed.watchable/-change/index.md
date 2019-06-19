[io.gladed.watchable](../index.md) / [Change](./index.md)

# Change

`interface Change`

An object that describes a change.

### Properties

| Name | Summary |
|---|---|
| [isInitial](is-initial.md) | `abstract val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |

### Extension Functions

| Name | Summary |
|---|---|
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [GroupChange](../-group-change/index.md) | `data class GroupChange : `[`Change`](./index.md)<br>A change to a single watchable in a group. |
| [HasSimpleChange](../-has-simple-change/index.md) | `interface HasSimpleChange<out S> : `[`Change`](./index.md)<br>A [Change](./index.md) that can be expressed in terms of simpler change objects of type [S](../-has-simple-change/index.md#S). |
| [ValueChange](../-value-change/index.md) | `data class ValueChange<T> : `[`Change`](./index.md)<br>Describes the update of a value. |
