[io.gladed.watchable](../index.md) / [MutableWatchableBase](index.md) / [use](./use.md)

# use

`open suspend fun <U> use(func: `[`M`](index.md#M)`.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)

Overrides [MutableWatchable.use](../-mutable-watchable/use.md)

Suspend until [func](../-mutable-watchable/use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](../-mutable-watchable/index.md#M) as desired and returning
the result. Note: if currently bound ([isBound](../-mutable-watchable/is-bound.md) returns true), attempts to modify [M](../-mutable-watchable/index.md#M) will throw.

