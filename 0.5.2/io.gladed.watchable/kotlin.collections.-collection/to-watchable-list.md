[io.gladed.watchable](../index.md) / [kotlin.collections.Collection](index.md) / [toWatchableList](./to-watchable-list.md)

# toWatchableList

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](to-watchable-list.md#T)`>.toWatchableList(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](to-watchable-list.md#T)`>`

Return a new [WatchableList](../-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html), watchable on the supplied [context](to-watchable-list.md#io.gladed.watchable$toWatchableList(kotlin.collections.Collection((io.gladed.watchable.toWatchableList.T)), kotlin.coroutines.CoroutineContext)/context).

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](to-watchable-list.md#T)`>.toWatchableList(scope: CoroutineScope): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](to-watchable-list.md#T)`>`

Return a new [WatchableList](../-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html), watchable on the supplied [scope](to-watchable-list.md#io.gladed.watchable$toWatchableList(kotlin.collections.Collection((io.gladed.watchable.toWatchableList.T)), kotlinx.coroutines.CoroutineScope)/scope).

