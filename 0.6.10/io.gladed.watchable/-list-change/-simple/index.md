[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Simple](./index.md)

# Simple

`data class Simple<T>`

The simplest form of a list change, affecting only a single position in the list.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Simple(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, remove: `[`T`](index.md#T)`? = null, add: `[`T`](index.md#T)`? = null)`<br>The simplest form of a list change, affecting only a single position in the list. |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`T`](index.md#T)`?`<br>Item added at [index](--index--.md) if any. |
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Index at which a change occurred. |
| [remove](remove.md) | `val remove: `[`T`](index.md#T)`?`<br>Item removed at [index](--index--.md) if any. |
