[io.gladed.watchable](../index.md) / [GroupChange](./index.md)

# GroupChange

`data class GroupChange : `[`Change`](../-change.md)

A change to a single watchable in a group.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GroupChange(watchable: `[`Watchable`](../-watchable/index.md)`<`[`Change`](../-change.md)`>, change: `[`Change`](../-change.md)`)`<br>A change to a single watchable in a group. |

### Properties

| Name | Summary |
|---|---|
| [change](change.md) | `val change: `[`Change`](../-change.md) |
| [watchable](watchable.md) | `val watchable: `[`Watchable`](../-watchable/index.md)`<`[`Change`](../-change.md)`>` |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
