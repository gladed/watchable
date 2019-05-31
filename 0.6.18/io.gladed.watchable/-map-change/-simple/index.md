[io.gladed.watchable](../../index.md) / [MapChange](../index.md) / [Simple](./index.md)

# Simple

`data class Simple<K, V>`

The simplified form of a [MapChange](../index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Simple(key: `[`K`](index.md#K)`, remove: `[`V`](index.md#V)`? = null, add: `[`V`](index.md#V)`? = null, isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`<br>The simplified form of a [MapChange](../index.md). |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`V`](index.md#V)`?`<br>Value added or updated for [key](key.md) if any. |
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True if this change came from an initial change. |
| [key](key.md) | `val key: `[`K`](index.md#K)<br>Key at which a change occurred. |
| [remove](remove.md) | `val remove: `[`V`](index.md#V)`?`<br>Value removed or replaced for [key](key.md) if any. |
