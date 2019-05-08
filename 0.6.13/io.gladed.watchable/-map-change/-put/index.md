[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Put](./index.md)

# Put

`data class Put<K, V> : `[`MapChange`](../index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>`

The addition of value [add](add.md), replacing value [remove](remove.md) if present, for [key](key.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Put(key: `[`K`](index.md#K)`, remove: `[`V`](index.md#V)`? = null, add: `[`V`](index.md#V)`)`<br>The addition of value [add](add.md), replacing value [remove](remove.md) if present, for [key](key.md). |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`V`](index.md#V)<br>New value for [key](key.md). |
| [key](key.md) | `val key: `[`K`](index.md#K) |
| [remove](remove.md) | `val remove: `[`V`](index.md#V)`?`<br>Old value for [key](key.md) if any. |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`MapChange.Simple`](../-simple/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>` |

### Inherited Properties

| Name | Summary |
|---|---|
| [isInitial](../is-initial.md) | `open val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
