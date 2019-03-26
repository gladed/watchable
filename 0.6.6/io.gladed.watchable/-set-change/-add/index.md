[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Add](./index.md)

# Add

`data class Add<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>`

A change representing the addition of an element to the set.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Add(added: `[`T`](index.md#T)`)`<br>A change representing the addition of an element to the set. |

### Properties

| Name | Summary |
|---|---|
| [added](added.md) | `val added: `[`T`](index.md#T) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SimpleChange`](../../-simple-change/index.md)`<`[`T`](index.md#T)`>>`<br>A list of [SimpleChange](../../-simple-change/index.md) objects that describe all parts of this change. |
