[io.gladed.watchable](index.md) / [ReadOnlyWatchableMap](./-read-only-watchable-map.md)

# ReadOnlyWatchableMap

`interface ReadOnlyWatchableMap<K, V> : `[`SimpleWatchable`](-simple-watchable/index.md)`<`[`MapChange.Simple`](-map-change/-simple/index.md)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>, `[`MapChange`](-map-change/index.md)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>>, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder.

### Inherited Functions

| Name | Summary |
|---|---|
| [simple](-simple-watchable/simple.md) | `open suspend fun simple(scope: CoroutineScope, func: suspend (`[`S`](-simple-watchable/index.md#S)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Watcher`](-watcher/index.md) |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableMap](kotlin.collections.-map/to-watchable-map.md) | `fun <K, V> `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](kotlin.collections.-map/to-watchable-map.md#K)`, `[`V`](kotlin.collections.-map/to-watchable-map.md#V)`>.toWatchableMap(): `[`WatchableMap`](-watchable-map/index.md)`<`[`K`](kotlin.collections.-map/to-watchable-map.md#K)`, `[`V`](kotlin.collections.-map/to-watchable-map.md#V)`>`<br>Return a new [WatchableMap](-watchable-map/index.md) containing the elements of this [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [toWatchableValue](to-watchable-value.md) | `fun <T> `[`T`](to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](-watchable-value/index.md)`<`[`T`](to-watchable-value.md#T)`>`<br>Convert this [T](to-watchable-value.md#T) to a watchable value of [T](to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableMap](-watchable-map/index.md) | `interface WatchableMap<K, V> : `[`MutableWatchable`](-mutable-watchable/index.md)`<`[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>, `[`MapChange`](-map-change/index.md)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>>, `[`ReadOnlyWatchableMap`](./-read-only-watchable-map.md)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](watchable-map-of.md) to create. |
