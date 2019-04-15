[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>, `[`Mergeable`](../../-mergeable/index.md)`<`[`SetChange`](../index.md)`<`[`T`](index.md#T)`>>`

A removal of items.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Remove(remove: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>)`<br>A removal of items. |

### Properties

| Name | Summary |
|---|---|
| [remove](remove.md) | `val remove: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>` |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SetChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |

### Functions

| Name | Summary |
|---|---|
| [merge](merge.md) | `fun merge(other: `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>): `[`SetChange.Remove`](./index.md)`<`[`T`](index.md#T)`>?` |
