[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watch](./watch.md)

# watch

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](watch.md#T)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](watch.md#T)`, `[`C`](watch.md#C)`>, block: (`[`C`](watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Return a [Job](#) that for the duration of this [CoroutineScope](#) invokes [handler](#) for any changes to [watchable](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable),
starting with its initial state.

