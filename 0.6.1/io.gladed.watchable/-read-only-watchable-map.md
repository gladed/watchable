[io.gladed.watchable](index.md) / [ReadOnlyWatchableMap](./-read-only-watchable-map.md)

# ReadOnlyWatchableMap

`interface ReadOnlyWatchableMap<K, V> : `[`Watchable`](-watchable/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>, `[`MapChange`](-map-change/index.md)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>>, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](-read-only-watchable-map.md#K)`, `[`V`](-read-only-watchable-map.md#V)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder.

### Inherited Properties

| Name | Summary |
|---|---|
| [value](-watchable/value.md) | `abstract val value: `[`T`](-watchable/index.md#T)<br>Return an immutable copy of the current value of [T](-watchable/index.md#T). |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver lists of changes for this [Watchable](-watchable/index.md) to [func](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [subscribe](-watchable/subscribe.md) | `abstract fun subscribe(scope: CoroutineScope): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>>`<br>Return a channel which will receive successive lists of changes as they occur. |
| [watch](-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes for this [Watchable](-watchable/index.md) to [func](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableMap](kotlin.collections.-map/to-watchable-map.md) | `fun <K, V> `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](kotlin.collections.-map/to-watchable-map.md#K)`, `[`V`](kotlin.collections.-map/to-watchable-map.md#V)`>.toWatchableMap(): `[`WatchableMap`](-watchable-map/index.md)`<`[`K`](kotlin.collections.-map/to-watchable-map.md#K)`, `[`V`](kotlin.collections.-map/to-watchable-map.md#V)`>`<br>Return a new [WatchableMap](-watchable-map/index.md) containing the elements of this [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableMap](-watchable-map/index.md) | `class WatchableMap<K, V> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>, `[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>, `[`MapChange`](-map-change/index.md)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>>, `[`ReadOnlyWatchableMap`](./-read-only-watchable-map.md)`<`[`K`](-watchable-map/index.md#K)`, `[`V`](-watchable-map/index.md#V)`>`<br>A [Watchable](-watchable/index.md) wrapper for a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](watchable-map-of.md) to create. |
