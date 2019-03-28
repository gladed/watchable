[io.gladed.watchable](../index.md) / [MutableWatchable](index.md) / [use](./use.md)

# use

`abstract suspend fun <U> use(func: `[`M`](index.md#M)`.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)

Suspend until [func](use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](index.md#M) as desired and returning
the result. Note: if currently bound ([isBound](is-bound.md) returns true), attempts to modify [M](index.md#M) will throw.

