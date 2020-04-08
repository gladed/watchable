[io.gladed.watchable.store](index.md) / [holding](./holding.md)

# holding

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](holding.md#T)`>.holding(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, containerPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = DEFAULT_CONTAINER_PERIOD, start: suspend `[`HoldBuilder`](-hold-builder/index.md)`.(`[`T`](holding.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`HoldingStore`](-holding-store/index.md)`<`[`T`](holding.md#T)`>`

Return a [HoldingStore](-holding-store/index.md) around this [Store](-store/index.md).

