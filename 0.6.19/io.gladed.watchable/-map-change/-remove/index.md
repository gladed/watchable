[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Remove](./index.md)

# Remove

`data class Remove<K, V> : `[`MapChange`](../index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>`

A removal of item [remove](remove.md) for [key](key.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Remove(key: `[`K`](index.md#K)`, remove: `[`V`](index.md#V)`)`<br>A removal of item [remove](remove.md) for [key](key.md). |

### Properties

| Name | Summary |
|---|---|
| [key](key.md) | `val key: `[`K`](index.md#K) |
| [remove](remove.md) | `val remove: `[`V`](index.md#V) |
| [simple](simple.md) | `val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`MapChange.Simple`](../-simple/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>` |

### Inherited Properties

| Name | Summary |
|---|---|
| [isInitial](../is-initial.md) | `open val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |
