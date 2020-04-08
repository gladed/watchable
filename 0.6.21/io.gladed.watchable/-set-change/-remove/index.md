[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>, `[`Addable`](../../-addable/index.md)`<`[`SetChange`](../index.md)`<`[`T`](index.md#T)`>>`

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

### Inherited Properties

| Name | Summary |
|---|---|
| [isInitial](../is-initial.md) | `open val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |

### Functions

| Name | Summary |
|---|---|
| [plus](plus.md) | `operator fun plus(other: `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>): `[`SetChange.Remove`](./index.md)`<`[`T`](index.md#T)`>?` |
