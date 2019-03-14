[io.gladed.watchable](index.md) / [ReadOnlyWatchableSet](./-read-only-watchable-set.md)

# ReadOnlyWatchableSet

`interface ReadOnlyWatchableSet<T> : `[`Watchable`](-watchable/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-read-only-watchable-set.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-read-only-watchable-set.md#T)`>>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder.

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver lists of changes for this [Watchable](-watchable/index.md) to [func](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the returned job is cancelled or the [scope](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [get](-watchable/get.md) | `abstract suspend fun get(): `[`T`](-watchable/index.md#T)<br>Return the current value of [T](-watchable/index.md#T). |
| [subscribe](-watchable/subscribe.md) | `abstract fun subscribe(scope: CoroutineScope): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>>`<br>Return a channel which will receive successive lists of changes as they occur. |
| [watch](-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: (`[`C`](-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes for this [Watchable](-watchable/index.md) to [func](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the returned job is cancelled or the [scope](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.Function1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableSet](-watchable-set/index.md) | `class WatchableSet<T> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](-watchable-set/index.md#T)`>, `[`SetChange`](-set-change/index.md)`<`[`T`](-watchable-set/index.md#T)`>>, `[`ReadOnlyWatchableSet`](./-read-only-watchable-set.md)`<`[`T`](-watchable-set/index.md#T)`>`<br>A [Watchable](-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](watchable-set-of.md) to create. |
