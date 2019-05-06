[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Initial](./index.md)

# Initial

`data class Initial<K, V> : `[`MapChange`](../index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>`

The initial state of the map.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Initial(map: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>)`<br>The initial state of the map. |

### Properties

| Name | Summary |
|---|---|
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
| [map](map.md) | `val map: `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>` |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`MapChange.Simple`](../-simple/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>` |
