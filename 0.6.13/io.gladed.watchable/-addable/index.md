[io.gladed.watchable](../index.md) / [Addable](./index.md)

# Addable

`interface Addable<T>`

Something that can be conditionally merged with something else.

### Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | `abstract operator fun plus(other: `[`T`](index.md#T)`): `[`T`](index.md#T)`?` |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [Add](../-set-change/-add/index.md) | `data class Add<T> : `[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-add/index.md#T)`>, `[`Addable`](./index.md)`<`[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-add/index.md#T)`>>`<br>An addition of items. |
| [Insert](../-list-change/-insert/index.md) | `data class Insert<T> : `[`ListChange`](../-list-change/index.md)`<`[`T`](../-list-change/-insert/index.md#T)`>, `[`Addable`](./index.md)`<`[`ListChange`](../-list-change/index.md)`<`[`T`](../-list-change/-insert/index.md#T)`>>`<br>An insertion of items at [index](../-list-change/-insert/--index--.md). |
| [Remove](../-set-change/-remove/index.md) | `data class Remove<T> : `[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-remove/index.md#T)`>, `[`Addable`](./index.md)`<`[`SetChange`](../-set-change/index.md)`<`[`T`](../-set-change/-remove/index.md#T)`>>`<br>A removal of items. |
