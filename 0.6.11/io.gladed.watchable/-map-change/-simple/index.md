[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Simple](./index.md)

# Simple

`data class Simple<K, V>`

The simplified form of a [MapChange](../index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Simple(key: `[`K`](index.md#K)`, remove: `[`V`](index.md#V)`? = null, add: `[`V`](index.md#V)`? = null)`<br>The simplified form of a [MapChange](../index.md). |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`V`](index.md#V)`?`<br>Value added or updated for [key](key.md) if any. |
| [key](key.md) | `val key: `[`K`](index.md#K)<br>Key at which a change occurred. |
| [remove](remove.md) | `val remove: `[`V`](index.md#V)`?`<br>Value removed or replaced for [key](key.md) if any. |
