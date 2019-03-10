[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [watch](./watch.md)

# watch

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](watch.md#T)`>> CoroutineScope.watch(watchable: `[`Watchable`](../-watchable/index.md)`<`[`T`](watch.md#T)`, `[`C`](watch.md#C)`>, func: (`[`C`](watch.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

On this [CoroutineScope](#), deliver changes on [watchable](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable) to [func](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/func), starting with its initial
state. Changes will stop arriving when this scope completes, when the [watchable](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/watchable)'s scope completes, when
the returned [Job](#) is cancelled, or if [func](watch.md#io.gladed.watchable$watch(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.watch.T, io.gladed.watchable.watch.C)), kotlin.Function1((io.gladed.watchable.watch.C, kotlin.Unit)))/func) throws.
