[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [subscribe](./subscribe.md)

# subscribe

`fun <T, C : `[`Change`](../-change.md)`<`[`T`](subscribe.md#T)`>> CoroutineScope.subscribe(target: `[`Watchable`](../-watchable/index.md)`<`[`T`](subscribe.md#T)`, `[`C`](subscribe.md#C)`>): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](subscribe.md#C)`>>`

Create a [ReceiveChannel](#) for intercepting lists of changes made to [target](subscribe.md#io.gladed.watchable$subscribe(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.subscribe.T, io.gladed.watchable.subscribe.C)))/target) for as long as this
[CoroutineScope](#) lives.

