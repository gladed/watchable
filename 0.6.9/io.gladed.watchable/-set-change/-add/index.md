[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Add](./index.md)

# Add

`data class Add<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>, `[`Addable`](../../-addable/index.md)`<`[`SetChange`](../index.md)`<`[`T`](index.md#T)`>>`

An addition of items.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Add(add: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>)`<br>An addition of items. |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>` |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SetChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |

### Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | `operator fun plus(other: `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>): `[`SetChange.Add`](./index.md)`<`[`T`](index.md#T)`>?` |
