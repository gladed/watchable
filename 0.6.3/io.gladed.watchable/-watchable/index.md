[io.gladed.watchable](../index.md) / [Watchable](./index.md)

# Watchable

`interface Watchable<T, C : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>>`

Wraps type [T](index.md#T) so that it may be watched for changes of type [C](index.md#C).

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T)<br>Return an immutable copy of the current value of [T](index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [batch](batch.md) | `abstract fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, consumer: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)<br>Initiate and consume a subscription for changes to this [Watchable](./index.md), returning a handle for control over the subscription. |
| [watch](watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`SubscriptionHandle`](../-subscription-handle/index.md)<br>Deliver changes for this [Watchable](./index.md) to [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the scope completes or the returned [SubscriptionHandle](../-subscription-handle/index.md) is closed. |

### Inheritors

| Name | Summary |
|---|---|
| [MutableWatchable](../-mutable-watchable/index.md) | `interface MutableWatchable<T, M : `[`T`](../-mutable-watchable/index.md#T)`, C : `[`Change`](../-change.md)`<`[`T`](../-mutable-watchable/index.md#T)`>> : `[`Watchable`](./index.md)`<`[`T`](../-mutable-watchable/index.md#T)`, `[`C`](../-mutable-watchable/index.md#C)`>`<br>A [Watchable](./index.md) which may be mutated in the form of an [M](../-mutable-watchable/index.md#M) and bound to other [Watchable](./index.md) sources. |
| [ReadOnlyWatchableList](../-read-only-watchable-list.md) | `interface ReadOnlyWatchableList<T> : `[`Watchable`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-read-only-watchable-list.md#T)`>, `[`ListChange`](../-list-change/index.md)`<`[`T`](../-read-only-watchable-list.md#T)`>>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-read-only-watchable-list.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableMap](../-read-only-watchable-map.md) | `interface ReadOnlyWatchableMap<K, V> : `[`Watchable`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>, `[`MapChange`](../-map-change/index.md)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>>, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableSet](../-read-only-watchable-set.md) | `interface ReadOnlyWatchableSet<T> : `[`Watchable`](./index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-read-only-watchable-set.md#T)`>, `[`SetChange`](../-set-change/index.md)`<`[`T`](../-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-read-only-watchable-set.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableValue](../-read-only-watchable-value.md) | `interface ReadOnlyWatchableValue<T> : `[`Watchable`](./index.md)`<`[`T`](../-read-only-watchable-value.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-read-only-watchable-value.md#T)`>>`<br>A [Watchable](./index.md) value of type [T](../-read-only-watchable-value.md#T) which may not be replaced by the reference holder. |
| [WatchableGroup](../-watchable-group/index.md) | `class WatchableGroup : `[`Watchable`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](./index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>, `[`GroupChange`](../-group-change/index.md)`>`<br>A group of [Watchable](./index.md) objects that can be watched for any change. |
