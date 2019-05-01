[io.gladed.watchable](../index.md) / [GroupChange](./index.md)

# GroupChange

`data class GroupChange : `[`Change`](../-change/index.md)

A change to a single watchable in a group.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GroupChange(watchable: `[`Watchable`](../-watchable/index.md)`<`[`Change`](../-change/index.md)`>, change: `[`Change`](../-change/index.md)`)`<br>A change to a single watchable in a group. |

### Properties

| Name | Summary |
|---|---|
| [change](change.md) | `val change: `[`Change`](../-change/index.md) |
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
| [watchable](watchable.md) | `val watchable: `[`Watchable`](../-watchable/index.md)`<`[`Change`](../-change/index.md)`>` |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
