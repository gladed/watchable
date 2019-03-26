[io.gladed.watchable](../index.md) / [Watchable](./index.md)

# Watchable

`interface Watchable<out T, out V, out C : `[`Change`](../-change/index.md)`<`[`T`](index.md#T)`, `[`V`](index.md#V)`>>`

Wraps container [T](index.md#T) (having objects of type [V](index.md#V)) so that it may be watched for changes (of type [C](index.md#C)).

Each watch operation takes a [CoroutineScope](#). Callbacks are delivered using this scope's context, and stop
automatically when this scope cancels or completes.

Each watch operation also returns a [WatchHandle](../-watch-handle/index.md) which may be used to independently cancel or join the watch
operation.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T)<br>Return an immutable copy of the current value of [T](index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [batch](batch.md) | `abstract fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](./index.md) to [func](batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func) as lists of [Change](../-change/index.md) objects. |
| [watch](watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](./index.md) to [func](watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func) as individual [Change](../-change/index.md) objects. |
| [watchSimple](watch-simple.md) | `open fun watchSimple(scope: CoroutineScope, func: suspend `[`SimpleChange`](../-simple-change/index.md)`<`[`V`](index.md#V)`>.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`WatchHandle`](../-watch-handle/index.md)<br>Deliver all changes from this [Watchable](./index.md) to [func](watch-simple.md#io.gladed.watchable.Watchable$watchSimple(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.SimpleChange((io.gladed.watchable.Watchable.V)), kotlin.Unit)))/func) receiving [SimpleChange](../-simple-change/index.md) objects. |

### Inheritors

| Name | Summary |
|---|---|
| [MutableWatchable](../-mutable-watchable/index.md) | `interface MutableWatchable<T, V, M : `[`T`](../-mutable-watchable/index.md#T)`, C : `[`Change`](../-change/index.md)`<`[`T`](../-mutable-watchable/index.md#T)`, `[`V`](../-mutable-watchable/index.md#V)`>> : `[`Watchable`](./index.md)`<`[`T`](../-mutable-watchable/index.md#T)`, `[`V`](../-mutable-watchable/index.md#V)`, `[`C`](../-mutable-watchable/index.md#C)`>`<br>A [Watchable](./index.md) which may be mutated in the form of an [M](../-mutable-watchable/index.md#M) and bound to other [Watchable](./index.md) sources. |
| [ReadOnlyWatchableList](../-read-only-watchable-list.md) | `interface ReadOnlyWatchableList<T> : `[`Watchable`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-read-only-watchable-list.md#T)`>, `[`T`](../-read-only-watchable-list.md#T)`, `[`ListChange`](../-list-change/index.md)`<`[`T`](../-read-only-watchable-list.md#T)`>>, `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-read-only-watchable-list.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableMap](../-read-only-watchable-map.md) | `interface ReadOnlyWatchableMap<K, out V> : `[`Watchable`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>, `[`V`](../-read-only-watchable-map.md#V)`, `[`MapChange`](../-map-change/index.md)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>>, `[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-read-only-watchable-map.md#K)`, `[`V`](../-read-only-watchable-map.md#V)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableSet](../-read-only-watchable-set.md) | `interface ReadOnlyWatchableSet<T> : `[`Watchable`](./index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-read-only-watchable-set.md#T)`>, `[`T`](../-read-only-watchable-set.md#T)`, `[`SetChange`](../-set-change/index.md)`<`[`T`](../-read-only-watchable-set.md#T)`>>, `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-read-only-watchable-set.md#T)`>`<br>A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder. |
| [ReadOnlyWatchableValue](../-read-only-watchable-value.md) | `interface ReadOnlyWatchableValue<T> : `[`Watchable`](./index.md)`<`[`T`](../-read-only-watchable-value.md#T)`, `[`T`](../-read-only-watchable-value.md#T)`, `[`ValueChange`](../-value-change/index.md)`<`[`T`](../-read-only-watchable-value.md#T)`>>`<br>A [Watchable](./index.md) value of type [T](../-read-only-watchable-value.md#T) which may not be replaced by the reference holder. |
| [WatchableGroup](../-watchable-group/index.md) | `class WatchableGroup<out T, out V, C : `[`Change`](../-change/index.md)`<`[`T`](../-watchable-group/index.md#T)`, `[`V`](../-watchable-group/index.md#V)`>> : `[`Watchable`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](./index.md)`<`[`T`](../-watchable-group/index.md#T)`, `[`V`](../-watchable-group/index.md#V)`, `[`C`](../-watchable-group/index.md#C)`>>, `[`Watchable`](./index.md)`<`[`T`](../-watchable-group/index.md#T)`, `[`V`](../-watchable-group/index.md#V)`, `[`C`](../-watchable-group/index.md#C)`>, `[`GroupChange`](../-group-change/index.md)`<`[`T`](../-watchable-group/index.md#T)`, `[`V`](../-watchable-group/index.md#V)`, `[`C`](../-watchable-group/index.md#C)`>>`<br>A group of [Watchable](./index.md) objects that can be watched for any change, which arrives as a [GroupChange](../-group-change/index.md). |
