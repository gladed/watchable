[io.gladed.watchable](../index.md) / [MutableWatchable](index.md) / [bind](./bind.md)

# bind

`abstract suspend fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match
[origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or
rebound.

`abstract suspend fun <C2 : `[`Change`](../-change.md)`> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C2`](bind.md#C2)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = IMMEDIATE, apply: `[`M`](index.md#M)`.(`[`C2`](bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied
to this object with [apply](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally
modified or rebound.

