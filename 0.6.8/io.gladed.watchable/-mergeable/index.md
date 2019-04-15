[io.gladed.watchable](../index.md) / [Mergeable](./index.md)

# Mergeable

`interface Mergeable<T>`

### Functions

| Name | Summary |
|---|---|
| [merge](merge.md) | `abstract fun merge(other: `[`T`](index.md#T)`): `[`T`](index.md#T)`?` |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [Add](../-set-change/-add/index.md) | `data class Add<T> : `[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-add/index.md#T)`>, `[`Mergeable`](./index.md)`<`[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-add/index.md#T)`>>`<br>An addition of items. |
| [Insert](../-list-change/-insert/index.md) | `data class Insert<T> : `[`ListChange`](../-list-change/index.md)`<`[`T`](../-list-change/-insert/index.md#T)`>, `[`Mergeable`](./index.md)`<`[`ListChange`](../-list-change/index.md)`<`[`T`](../-list-change/-insert/index.md#T)`>>`<br>An insertion of items at [index](../-list-change/-insert/--index--.md). |
| [Remove](../-set-change/-remove/index.md) | `data class Remove<T> : `[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-remove/index.md#T)`>, `[`Mergeable`](./index.md)`<`[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-remove/index.md#T)`>>`<br>A removal of items. |
