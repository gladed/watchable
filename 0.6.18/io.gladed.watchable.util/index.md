[io.gladed.watchable.util](./index.md)

## Package io.gladed.watchable.util

### Types

| Name | Summary |
|---|---|
| [Guard](-guard/index.md) | `interface Guard<T>`<br>Provides mutually-exclusive access to a value of [T](-guard/index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [guard](guard.md) | `fun <T> `[`T`](guard.md#T)`.guard(): `[`Guard`](-guard/index.md)`<`[`T`](guard.md#T)`>`<br>Return [T](guard.md#T) surrounded by a [Guard](-guard/index.md). |
| [lazyGuard](lazy-guard.md) | `fun <T> lazyGuard(create: () -> `[`T`](lazy-guard.md#T)`): `[`Guard`](-guard/index.md)`<`[`T`](lazy-guard.md#T)`>`<br>Return a [Guard](-guard/index.md) that will lazily [create](lazy-guard.md#T) on first use. |
