[io.gladed.watchable](../index.md) / [MutableWatchableBase](index.md) / [bind](./bind.md)

# bind

`open fun bind(origin: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Overrides [MutableWatchable.bind](../-mutable-watchable/bind.md)

Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match
[origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) exactly. This object may not be modified while bound. When this object's [CoroutineScope](#) completes,
no further binding related changes are applied. Bindings may not be circular.

`open fun <T2, C2 : `[`Change`](../-change.md)`<`[`T2`](bind.md#T2)`>> bind(origin: `[`Watchable`](../-watchable/index.md)`<`[`T2`](bind.md#T2)`, `[`C2`](bind.md#C2)`>, apply: `[`M`](index.md#M)`.(`[`C2`](bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Overrides [MutableWatchable.bind](../-mutable-watchable/bind.md)

Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the mutable form of this
object is updated with [apply](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply). This object may not be otherwise modified while bound. When this object's
[CoroutineScope](#) completes, apply is no longer invoked. Bindings may not be circular.

