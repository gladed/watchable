[io.gladed.watchable](index.md) / [ReadOnlyWatchableList](./-read-only-watchable-list.md)

# ReadOnlyWatchableList

`interface ReadOnlyWatchableList<T> : `[`Watchable`](-watchable/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-read-only-watchable-list.md#T)`>, `[`ListChange`](-list-change/index.md)`<`[`T`](-read-only-watchable-list.md#T)`>>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-read-only-watchable-list.md#T)`>`

A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder.

### Inherited Properties

| Name | Summary |
|---|---|
| [value](-watchable/value.md) | `abstract val value: `[`T`](-watchable/index.md#T)<br>Return an immutable copy of the current value of [T](-watchable/index.md#T). |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](-subscription-handle/index.md)<br>Deliver lists of changes for this [Watchable](-watchable/index.md) to [func](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the [scope](-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes or the returned [SubscriptionHandle](-subscription-handle/index.md) is closed. |
| [subscribe](-watchable/subscribe.md) | `abstract fun subscribe(scope: CoroutineScope, consumer: `[`Subscription`](-subscription/index.md)`<`[`C`](-watchable/index.md#C)`>.() -> `[`SubscriptionHandle`](-subscription-handle/index.md)`): `[`SubscriptionHandle`](-subscription-handle/index.md)<br>Initiate and consume a subscription for changes to this [Watchable](-watchable/index.md), returning a handle for control over the subscription. |
| [watch](-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](-subscription-handle/index.md)<br>Deliver changes for this [Watchable](-watchable/index.md) to [func](-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the scope completes or the returned [SubscriptionHandle](-subscription-handle/index.md) is closed. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableList](kotlin.collections.-collection/to-watchable-list.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](-watchable-list/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableSet](kotlin.collections.-collection/to-watchable-set.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](-watchable-set/index.md)`<`[`T`](kotlin.collections.-collection/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](-watchable-set/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |

### Inheritors

| Name | Summary |
|---|---|
| [WatchableList](-watchable-list/index.md) | `class WatchableList<T> : `[`MutableWatchableBase`](-mutable-watchable-base/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-watchable-list/index.md#T)`>, `[`MutableList`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)`<`[`T`](-watchable-list/index.md#T)`>, `[`ListChange`](-list-change/index.md)`<`[`T`](-watchable-list/index.md#T)`>>, `[`ReadOnlyWatchableList`](./-read-only-watchable-list.md)`<`[`T`](-watchable-list/index.md#T)`>`<br>A [Watchable](-watchable/index.md) wrapper for a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](watchable-list-of.md) to create. |
