[io.gladed.watchable](../../index.md) / [ListChange](../index.md) / [Simple](./index.md)

# Simple

`data class Simple<T>`

The simplest form of a list change, affecting only a single position in the list.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Simple(index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, remove: `[`T`](index.md#T)`? = null, add: `[`T`](index.md#T)`? = null, isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`<br>The simplest form of a list change, affecting only a single position in the list. |

### Properties

| Name | Summary |
|---|---|
| [add](add.md) | `val add: `[`T`](index.md#T)`?`<br>Item added at [index](--index--.md) if any. |
| [index](--index--.md) | `val index: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Index at which a change occurred. |
| [isInitial](is-initial.md) | `val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Originally came from an initial change. |
| [remove](remove.md) | `val remove: `[`T`](index.md#T)`?`<br>Item removed at [index](--index--.md) if any. |
