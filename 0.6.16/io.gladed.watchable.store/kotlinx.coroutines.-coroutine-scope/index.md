[io.gladed.watchable.store](../index.md) / [kotlinx.coroutines.CoroutineScope](./index.md)

### Extensions for kotlinx.coroutines.CoroutineScope

| Name | Summary |
|---|---|
| [holding](holding.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> CoroutineScope.holding(store: `[`Store`](../-store/index.md)`<`[`T`](holding.md#T)`>, containerPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = DEFAULT_CONTAINER_PERIOD, start: suspend `[`HoldBuilder`](../-hold-builder/index.md)`.(`[`T`](holding.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`HoldingStore`](../-holding-store/index.md)`<`[`T`](holding.md#T)`>`<br>Return a [HoldingStore](../-holding-store/index.md) for this [CoroutineScope](#) around [store](holding.md#io.gladed.watchable.store$holding(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.store.Store((io.gladed.watchable.store.holding.T)), kotlin.Long, kotlin.coroutines.SuspendFunction2((io.gladed.watchable.store.HoldBuilder, io.gladed.watchable.store.holding.T, kotlin.Unit)))/store). |
