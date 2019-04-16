[io.gladed.watchable](index.md) / [Change](./-change.md)

# Change

`interface Change`

An object that describes a change.

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](to-watchable-value.md) | `fun <T> `[`T`](to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](-watchable-value/index.md)`<`[`T`](to-watchable-value.md#T)`>`<br>Convert this [T](to-watchable-value.md#T) to a watchable value of [T](to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [GroupChange](-group-change/index.md) | `data class GroupChange : `[`Change`](./-change.md)<br>A change to a single watchable in a group. |
| [HasSimpleChange](-has-simple-change/index.md) | `interface HasSimpleChange<out S> : `[`Change`](./-change.md)<br>A [Change](./-change.md) that can be expressed in terms of simpler change objects of type [S](-has-simple-change/index.md#S). |
| [ValueChange](-value-change/index.md) | `data class ValueChange<T> : `[`Change`](./-change.md)<br>Describes the update of a value. |
