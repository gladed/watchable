[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<T> : `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>`

A removal of an element in the list.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Remove(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, removed: `[`T`](index.md#T)`)`<br>A removal of an element in the list. |

### Properties

| Name | Summary |
|---|---|
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [removed](removed.md) | `val removed: `[`T`](index.md#T) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SimpleChange`](../../-simple-change/index.md)`<`[`T`](index.md#T)`>>`<br>A list of [SimpleChange](../../-simple-change/index.md) objects that describe all parts of this change. |
