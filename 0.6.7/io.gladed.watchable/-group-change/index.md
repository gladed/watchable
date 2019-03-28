[io.gladed.watchable](../index.md) / [GroupChange](./index.md)

# GroupChange

`data class GroupChange<out T, out V, C : `[`Change`](../-change/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`>> : `[`Change`](../-change/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>, `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>`

A single change to a single watchable in a group.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GroupChange(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>, change: `[`C`](index.md#C)`)`<br>A single change to a single watchable in a group. |

### Properties

| Name | Summary |
|---|---|
| [change](change.md) | `val change: `[`C`](index.md#C) |
| [simple](simple.md) | `val simple: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SimpleChange`](../-simple-change/index.md)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>>>`<br>A list of [SimpleChange](../-simple-change/index.md) objects that describe all parts of this change. |
| [watchable](watchable.md) | `val watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`, `[`C`](index.md#C)`>` |
