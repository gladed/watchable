[io.gladed.watchable.util](index.md) / [lazyGuard](./lazy-guard.md)

# lazyGuard

`fun <T> lazyGuard(create: suspend () -> `[`T`](lazy-guard.md#T)`): `[`Guard`](-guard/index.md)`<`[`T`](lazy-guard.md#T)`>`

Return a [Guard](-guard/index.md) that will lazily [create](lazy-guard.md#T) on first use.

