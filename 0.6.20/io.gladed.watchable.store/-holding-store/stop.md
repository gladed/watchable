[io.gladed.watchable.store](../index.md) / [HoldingStore](index.md) / [stop](./stop.md)

# stop

`suspend fun stop(scope: CoroutineScope, key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

If any item corresponding to [key](stop.md#io.gladed.watchable.store.HoldingStore$stop(kotlinx.coroutines.CoroutineScope, kotlin.String)/key) is currently held by [scope](stop.md#io.gladed.watchable.store.HoldingStore$stop(kotlinx.coroutines.CoroutineScope, kotlin.String)/scope), stop holding it.

`suspend fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Release everything regardless of the state of scopes.

