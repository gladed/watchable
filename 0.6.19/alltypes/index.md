

### All Types

| Name | Summary |
|---|---|
| [io.gladed.watchable.Addable](../io.gladed.watchable/-addable/index.md) | Something that can be conditionally merged with something else. |
| [io.gladed.watchable.store.Cache](../io.gladed.watchable.store/-cache/index.md) | A RAM cache that prevents overuse of backing store by serving objects it already has loaded. |
| [io.gladed.watchable.store.Cannot](../io.gladed.watchable.store/-cannot/index.md) | Something cannot be done. |
| [io.gladed.watchable.Change](../io.gladed.watchable/-change/index.md) | An object that describes a change. |
| [io.gladed.watchable.store.Container](../io.gladed.watchable.store/-container/index.md) | An object containing a watchable representing changes for a whole object. |
| [kotlinx.coroutines.CoroutineScope](../io.gladed.watchable/kotlinx.coroutines.-coroutine-scope/index.md) (extensions in package io.gladed.watchable) |  |
| [kotlinx.coroutines.CoroutineScope](../io.gladed.watchable.store/kotlinx.coroutines.-coroutine-scope/index.md) (extensions in package io.gladed.watchable.store) |  |
| [io.gladed.watchable.store.FileStore](../io.gladed.watchable.store/-file-store/index.md) | Read/write strings to files for each key. |
| [io.gladed.watchable.GroupChange](../io.gladed.watchable/-group-change/index.md) | A change to a single watchable in a group. |
| [io.gladed.watchable.util.Guard](../io.gladed.watchable.util/-guard/index.md) | Provides mutually-exclusive access to a value of [T](../io.gladed.watchable.util/-guard/index.md#T). |
| [io.gladed.watchable.HasSimpleChange](../io.gladed.watchable/-has-simple-change/index.md) | A [Change](../io.gladed.watchable/-change/index.md) that can be expressed in terms of simpler change objects of type [S](../io.gladed.watchable/-has-simple-change/index.md#S). |
| [io.gladed.watchable.store.Hold](../io.gladed.watchable.store/-hold/index.md) | Represents resources held on behalf of an object. |
| [io.gladed.watchable.store.HoldBuilder](../io.gladed.watchable.store/-hold-builder/index.md) | DSL for creating a [Hold](../io.gladed.watchable.store/-hold/index.md). |
| [io.gladed.watchable.store.HoldingStore](../io.gladed.watchable.store/-holding-store/index.md) | A [Store](../io.gladed.watchable.store/-store/index.md) factory, producing stores that trigger operations on its items while those objects are in use. |
| [kotlin.collections.Iterable](../io.gladed.watchable/kotlin.collections.-iterable/index.md) (extensions in package io.gladed.watchable) |  |
| [io.gladed.watchable.ListChange](../io.gladed.watchable/-list-change/index.md) | Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). |
| [kotlin.collections.Map](../io.gladed.watchable/kotlin.collections.-map/index.md) (extensions in package io.gladed.watchable) |  |
| [io.gladed.watchable.MapChange](../io.gladed.watchable/-map-change/index.md) | Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [io.gladed.watchable.store.MemoryStore](../io.gladed.watchable.store/-memory-store/index.md) | A store entirely in RAM. |
| [io.gladed.watchable.MutableValue](../io.gladed.watchable/-mutable-value/index.md) | A container for a value that can be changed. |
| [io.gladed.watchable.MutableWatchable](../io.gladed.watchable/-mutable-watchable/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) containing a mutable object of type [M](../io.gladed.watchable/-mutable-watchable/index.md#M), which can both generate and accept changes of type [C](../io.gladed.watchable/-mutable-watchable/index.md#C). |
| [io.gladed.watchable.Period](../io.gladed.watchable/-period/index.md) | Defines special values for watcher timing. |
| [io.gladed.watchable.ReadOnlyWatchableList](../io.gladed.watchable/-read-only-watchable-list.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableMap](../io.gladed.watchable/-read-only-watchable-map.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableSet](../io.gladed.watchable/-read-only-watchable-set.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may not be modified by the reference holder. |
| [io.gladed.watchable.ReadOnlyWatchableValue](../io.gladed.watchable/-read-only-watchable-value/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) value of type [T](../io.gladed.watchable/-read-only-watchable-value/index.md#T) which may not be replaced by the reference holder. |
| [io.gladed.watchable.SetChange](../io.gladed.watchable/-set-change/index.md) | Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html). |
| [io.gladed.watchable.SimpleWatchable](../io.gladed.watchable/-simple-watchable/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) that allows for a more verbose series of simpler changes. |
| [io.gladed.watchable.store.Store](../io.gladed.watchable.store/-store/index.md) | An object that retrieves elements by a String key. |
| [io.gladed.watchable.store.Transformer](../io.gladed.watchable.store/-transformer/index.md) | Convert between source ([S](../io.gladed.watchable.store/-transformer/index.md#S)) and target ([T](../io.gladed.watchable.store/-transformer/index.md#T)) forms of an object. |
| [io.gladed.watchable.Value](../io.gladed.watchable/-value/index.md) | A read-only wrapper for a value of [T](../io.gladed.watchable/-value/index.md#T). |
| [io.gladed.watchable.ValueChange](../io.gladed.watchable/-value-change/index.md) | Describes the update of a value. |
| [io.gladed.watchable.Watchable](../io.gladed.watchable/-watchable/index.md) | An object that allows you to watch for changes of type [C](../io.gladed.watchable/-watchable/index.md#C). |
| [io.gladed.watchable.WatchableBase](../io.gladed.watchable/-watchable-base/index.md) | Base for an object that generates change events of type [C](../io.gladed.watchable/-watchable-base/index.md#C) as its underlying data changes. |
| [io.gladed.watchable.WatchableGroup](../io.gladed.watchable/-watchable-group/index.md) | A group of [Watchable](../io.gladed.watchable/-watchable/index.md) objects that can be watched for any change, which arrives as a [GroupChange](../io.gladed.watchable/-group-change/index.md). Can also be created with [group](../io.gladed.watchable/group.md). |
| [io.gladed.watchable.WatchableList](../io.gladed.watchable/-watchable-list/index.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) which may also be modified or bound. Use [watchableListOf](../io.gladed.watchable/watchable-list-of.md) to create. |
| [io.gladed.watchable.WatchableMap](../io.gladed.watchable/-watchable-map/index.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) which may also be modified or bound. Use [watchableMapOf](../io.gladed.watchable/watchable-map-of.md) to create. |
| [io.gladed.watchable.WatchableSet](../io.gladed.watchable/-watchable-set/index.md) | A [Watchable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](../io.gladed.watchable/watchable-set-of.md) to create. |
| [io.gladed.watchable.WatchableValue](../io.gladed.watchable/-watchable-value/index.md) | A [Watchable](../io.gladed.watchable/-watchable/index.md) value of [T](../io.gladed.watchable/-watchable-value/index.md#T) which may also be replaced or bound. Use [watchableValueOf](../io.gladed.watchable/watchable-value-of.md) to create. |
| [io.gladed.watchable.Watcher](../io.gladed.watchable/-watcher/index.md) | An ongoing watch operation. |
