[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>`

A change representing the removal of an element from the set.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Remove(removed: `[`T`](index.md#T)`)`<br>A change representing the removal of an element from the set. |

### Properties

| Name | Summary |
|---|---|
| [removed](removed.md) | `val removed: `[`T`](index.md#T) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SimpleChange`](../../-simple-change/index.md)`<`[`T`](index.md#T)`>>`<br>A list of [SimpleChange](../../-simple-change/index.md) objects that describe all parts of this change. |
