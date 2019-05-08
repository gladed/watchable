[io.gladed.watchable](../../index.md) / [SetChange](../index.md) / [Initial](./index.md)

# Initial

`data class Initial<T> : `[`SetChange`](../index.md)`<`[`T`](index.md#T)`>`

The initial state of the set.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Initial(set: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>)`<br>The initial state of the set. |

### Properties

| Name | Summary |
|---|---|
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
| [set](set.md) | `val set: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>` |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`SetChange.Simple`](../-simple/index.md)`<`[`T`](index.md#T)`>>` |
