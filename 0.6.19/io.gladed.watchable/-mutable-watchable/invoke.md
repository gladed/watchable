[io.gladed.watchable](../index.md) / [MutableWatchable](index.md) / [invoke](./invoke.md)

# invoke

`abstract suspend operator fun <U> invoke(func: `[`M`](index.md#M)`.() -> `[`U`](invoke.md#U)`): `[`U`](invoke.md#U)

Suspend until [func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func) can safely execute on the mutable form [M](index.md#M) of this watchable, returning [func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func)'s result.
[func](invoke.md#io.gladed.watchable.MutableWatchable$invoke(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.invoke.U)))/func) must not block or return the mutable form outside of this routine.

