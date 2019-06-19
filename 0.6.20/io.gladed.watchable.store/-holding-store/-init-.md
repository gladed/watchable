[io.gladed.watchable.store](../index.md) / [HoldingStore](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`HoldingStore(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, back: `[`Store`](../-store/index.md)`<`[`T`](index.md#T)`>, containerPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = DEFAULT_CONTAINER_PERIOD, createHold: suspend `[`HoldBuilder`](../-hold-builder/index.md)`.(`[`T`](index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)`

A [Store](../-store/index.md) factory, producing stores that trigger operations on its items while those objects are in use.

For any new object retrieved, [createHold](#) is called to construct a [Hold](../-hold/index.md) on the object. The [Hold](../-hold/index.md) remains
until all scopes using the object complete.

