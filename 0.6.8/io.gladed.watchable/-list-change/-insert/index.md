[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Insert](./index.md)

# Insert

`data class Insert<T> : `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>, `[`Mergeable`](../../-mergeable/index.md)`<`[`ListChange`](../index.md)`<`[`T`](index.md#T)`>>`

An insertion of items at [index](--index--.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Insert(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, insert: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>)`<br>An insertion of items at [index](--index--.md). |

### Properties

| Name | Summary |
|---|---|
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [insert](insert.md) | `val insert: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>` |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ListChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |

### Functions

| Name | Summary |
|---|---|
| [merge](merge.md) | `fun merge(other: `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>): `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>?` |
