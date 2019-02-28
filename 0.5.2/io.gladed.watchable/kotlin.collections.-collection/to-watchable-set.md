[io.gladed.watchable](../index.md) / [kotlin.collections.Collection](index.md) / [toWatchableSet](./to-watchable-set.md)

# toWatchableSet

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](to-watchable-set.md#T)`>.toWatchableSet(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`WatchableSet`](../-watchable-set/index.md)`<`[`T`](to-watchable-set.md#T)`>`

Return a new [WatchableSet](../-watchable-set/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html), watchable on the supplied [context](to-watchable-set.md#io.gladed.watchable$toWatchableSet(kotlin.collections.Collection((io.gladed.watchable.toWatchableSet.T)), kotlin.coroutines.CoroutineContext)/context).

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](to-watchable-set.md#T)`>.toWatchableSet(scope: CoroutineScope): `[`WatchableSet`](../-watchable-set/index.md)`<`[`T`](to-watchable-set.md#T)`>`

Return a new [WatchableSet](../-watchable-set/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html), living  on the supplied [scope](to-watchable-set.md#io.gladed.watchable$toWatchableSet(kotlin.collections.Collection((io.gladed.watchable.toWatchableSet.T)), kotlinx.coroutines.CoroutineScope)/scope).

