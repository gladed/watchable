[io.gladed.watchable](../index.md) / [WatchableGroup](./index.md)

# WatchableGroup

`class WatchableGroup : `[`Watchable`](../-watchable/index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>, `[`GroupChange`](../-group-change/index.md)`>`

A group of [Watchable](../-watchable/index.md) objects that can be watched for any change.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WatchableGroup(watchables: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>)`<br>A group of [Watchable](../-watchable/index.md) objects that can be watched for any change. |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, out `[`Change`](../-change.md)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>>>`<br>Return an immutable copy of the current value of [T](../-watchable/index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [subscribe](subscribe.md) | `fun subscribe(scope: CoroutineScope): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`GroupChange`](../-group-change/index.md)`>>`<br>Return a channel which will receive successive lists of changes as they occur. |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](../-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver lists of changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |
