[io.gladed.watchable.util](./index.md)

## Package io.gladed.watchable.util

### Types

| Name | Summary |
|---|---|
| [Guard](-guard/index.md) | `interface Guard<T>`<br>Provides mutually-exclusive access to a value of [T](-guard/index.md#T). |
| [Try](-try/index.md) | `sealed class Try<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>The result of a suspending attempt to do something that might not be possible (e.g. could throw [Cannot](-cannot/index.md)). |

### Exceptions

| Name | Summary |
|---|---|
| [Cannot](-cannot/index.md) | `open class Cannot : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Something cannot be done. |

### Functions

| Name | Summary |
|---|---|
| [guard](guard.md) | `fun <T> `[`T`](guard.md#T)`.guard(): `[`Guard`](-guard/index.md)`<`[`T`](guard.md#T)`>`<br>Return [T](guard.md#T) surrounded by a [Guard](-guard/index.md). |
| [lazyGuard](lazy-guard.md) | `fun <T> lazyGuard(create: suspend () -> `[`T`](lazy-guard.md#T)`): `[`Guard`](-guard/index.md)`<`[`T`](lazy-guard.md#T)`>`<br>Return a [Guard](-guard/index.md) that will lazily [create](lazy-guard.md#T) on first use. |
