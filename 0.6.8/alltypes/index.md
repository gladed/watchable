

### All Types

| Name | Summary |
|---|---|
| [io.gladed.watchable.Change](../io.gladed.watchable/-change.md) | An object that describes a change. |
| [kotlin.collections.Collection](../io.gladed.watchable/kotlin.collections.-collection/index.md) (extensions in package io.gladed.watchable) |  |
| [kotlinx.coroutines.CoroutineScope](../io.gladed.watchable/kotlinx.coroutines.-coroutine-scope/index.md) (extensions in package io.gladed.watchable) |  |
| [io.gladed.watchable.GroupChange](../io.gladed.watchable/-group-change/index.md) | A change to a single watchable in a group. |
| [io.gladed.watchable.util.Guard](../io.gladed.watchable.util/-guard/index.md) | Protects all access to [item](#) behind a [Mutex](#). |
| [io.gladed.watchable.HasSimpleChange](../io.gladed.watchable/-has-simple-change/index.md) | A [Change](../io.gladed.watchable/-change.md) that can be expressed in terms of simpler change objects of type [S](../io.gladed.watchable/-has-simple-change/index.md#S). |
| [io.gladed.watchable.ListChange](../io.gladed.watchable/-list-change/index.md) | Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). |
| [kotlin.collections.Map](../io.gladed.watchable/kotlin.collections.-map/index.md) (extensions in package io.gladed.watchable) |  |
| [io.gladed.watchable.MapChange](../io.gladed.watchable/-map-change/index.md) | Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [io.gladed.watchable.Mergeable](../io.gladed.watchable/-mergeable/index.md) |  |
| [io.gladed.watchable.MutableValue](../io.gladed.watchable/-mutable-value/index.md) | A container for a value that can be changed. |
| [io.gladed.watchable.MutableWatchable](../io.gladed.watchable/-mutable-watchable/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) containing a mutable object of type [M](../io.gladed.watchable/-mutable-watchable/index.md#M), which can both generate and accept changes of type [C](../io.gladed.watchable/-mutable-watchable/index.md#C). |
| [io.gladed.watchable.MutableWatchableBase](../io.gladed.watchable/-mutable-watchable-base/index.md) | Base for implementing a type that is watchable, mutable, and bindable. |
| [io.gladed.watchable.Period](../io.gladed.watchable/-period/index.md) | Defines special values for watcher timing. |
| [io.gladed.watchable.ReadOnlyWatchableList](../io.gladed.watchable/-read-only-watchable-list.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableMap](../io.gladed.watchable/-read-only-watchable-map.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableSet](../io.gladed.watchable/-read-only-watchable-set.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableValue](../io.gladed.watchable/-read-only-watchable-value/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) value of type [T](../io.gladed.watchable/-read-only-watchable-value/index.md#T) which may not be replaced by the reference holder. |
| [io.gladed.watchable.SetChange](../io.gladed.watchable/-set-change/index.md) | Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html). |
| [io.gladed.watchable.SimpleWatchable](../io.gladed.watchable/-simple-watchable/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) that allows for a more verbose series of simpler changes. |
| [io.gladed.watchable.TestContextWrapper](../io.gladed.watchable/-test-context-wrapper/index.md) | A wrapper for [testContext](../io.gladed.watchable/-test-context-wrapper/test-context.md) so that it can be added into a [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html). |
| [io.gladed.watchable.Value](../io.gladed.watchable/-value/index.md) | A read-only wrapper for a value of [T](../io.gladed.watchable/-value/index.md#T). |
| [io.gladed.watchable.ValueChange](../io.gladed.watchable/-value-change/index.md) | Describes the update of a value. |
| [io.gladed.watchable.Watchable](../io.gladed.watchable/-watchable/index.md) | An object that allows you to watch for changes of type [C](../io.gladed.watchable/-watchable/index.md#C). |
| [io.gladed.watchable.WatchableBase](../io.gladed.watchable/-watchable-base/index.md) | Base for an object that generates change events of type [C](../io.gladed.watchable/-watchable-base/index.md#C) as its underlying data changes. |
| [io.gladed.watchable.WatchableGroup](../io.gladed.watchable/-watchable-group/index.md) | A group of [Watchable](../io.gladed.watchable/-watchable/index.md) objects that can be watched for any change, which arrives as a [GroupChange](../io.gladed.watchable/-group-change/index.md). |
| [io.gladed.watchable.WatchableList](../io.gladed.watchable/-watchable-list/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) wrapper for a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](../io.gladed.watchable/watchable-list-of.md) to create. |
| [io.gladed.watchable.WatchableMap](../io.gladed.watchable/-watchable-map/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) wrapper for a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](../io.gladed.watchable/watchable-map-of.md) to create. |
| [io.gladed.watchable.WatchableSet](../io.gladed.watchable/-watchable-set/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](../io.gladed.watchable/watchable-set-of.md) to create. |
| [io.gladed.watchable.WatchableValue](../io.gladed.watchable/-watchable-value/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) value of [T](../io.gladed.watchable/-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../io.gladed.watchable/watchable-value-of.md) to create. |
| [io.gladed.watchable.Watcher](../io.gladed.watchable/-watcher/index.md) | An ongoing watch operation that can be closed or cancelled. |
