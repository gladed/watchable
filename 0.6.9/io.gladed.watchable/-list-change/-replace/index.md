[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Replace](./index.md)

# Replace

`data class Replace<T> : `[`ListChange`](../index.md)`<`[`T`](index.md#T)`>`

An overwriting of [remove](remove.md) at [index](--index--.md), replacing with [add](add.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Replace(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, remove: `[`T`](index.md#T)`, add: `[`T`](index.md#T)`)`<br>An overwriting of [remove](remove.md) at [index](--index--.md), replacing with [add](add.md). |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`T`](index.md#T) |
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [remove](remove.md) | `val remove: `[`T`](index.md#T) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ListChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |
