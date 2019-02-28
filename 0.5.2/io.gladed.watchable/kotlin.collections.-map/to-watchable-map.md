[io.gladed.watchable](../index.md) / [kotlin.collections.Map](index.md) / [toWatchableMap](./to-watchable-map.md)

# toWatchableMap

`fun <K, V> `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](to-watchable-map.md#K)`, `[`V`](to-watchable-map.md#V)`>.toWatchableMap(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`WatchableMap`](../-watchable-map/index.md)`<`[`K`](to-watchable-map.md#K)`, `[`V`](to-watchable-map.md#V)`>`

Return a new [WatchableMap](../-watchable-map/index.md) containing the elements of this [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html), watchable on the supplied [context](to-watchable-map.md#io.gladed.watchable$toWatchableMap(kotlin.collections.Map((io.gladed.watchable.toWatchableMap.K, io.gladed.watchable.toWatchableMap.V)), kotlin.coroutines.CoroutineContext)/context).

`fun <K, V> `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](to-watchable-map.md#K)`, `[`V`](to-watchable-map.md#V)`>.toWatchableMap(scope: CoroutineScope): `[`WatchableMap`](../-watchable-map/index.md)`<`[`K`](to-watchable-map.md#K)`, `[`V`](to-watchable-map.md#V)`>`

Return a new [WatchableMap](../-watchable-map/index.md) containing the elements of this [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html), watchable on the supplied [scope](to-watchable-map.md#io.gladed.watchable$toWatchableMap(kotlin.collections.Map((io.gladed.watchable.toWatchableMap.K, io.gladed.watchable.toWatchableMap.V)), kotlinx.coroutines.CoroutineScope)/scope).

