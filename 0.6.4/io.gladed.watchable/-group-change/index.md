[io.gladed.watchable](../index.md) / [GroupChange](./index.md)

# GroupChange

`data class GroupChange : `[`Change`](../-change.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>>`

A single change to a single watchable in a group.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `GroupChange(watchable: `[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>, change: `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>)`<br>A single change to a single watchable in a group. |

### Properties

| Name | Summary |
|---|---|
| [change](change.md) | `val change: `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |
| [watchable](watchable.md) | `val watchable: `[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>` |
