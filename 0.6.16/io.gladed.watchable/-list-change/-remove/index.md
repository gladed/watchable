[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<T> : `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>`

A removal of [remove](remove.md) at [index](--index--.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Remove(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, remove: `[`T`](index.md#T)`)`<br>A removal of [remove](remove.md) at [index](--index--.md). |

### Properties

| Name | Summary |
|---|---|
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [remove](remove.md) | `val remove: `[`T`](index.md#T) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ListChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |

### Inherited Properties

| Name | Summary |
|---|---|
| [isInitial](../is-initial.md) | `open val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
