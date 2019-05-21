[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Replace](./index.md)

# Replace

`data class Replace<K, V> : `[`MapChange`](../index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>`

A change representing the replacement of an element for a key in the map.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Replace(key: `[`K`](index.md#K)`, removed: `[`V`](index.md#V)`, added: `[`V`](index.md#V)`)`<br>A change representing the replacement of an element for a key in the map. |

### Properties

| Name | Summary |
|---|---|
| [added](added.md) | `val added: `[`V`](index.md#V) |
| [key](key.md) | `val key: `[`K`](index.md#K) |
| [removed](removed.md) | `val removed: `[`V`](index.md#V) |