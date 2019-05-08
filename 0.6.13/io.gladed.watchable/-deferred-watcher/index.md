[io.gladed.watchable](../index.md) / [DeferredWatcher](./index.md)

# DeferredWatcher

`open class DeferredWatcher : `[`Watcher`](../-watcher/index.md)

Exposes a [deferred](#) as a [Watcher](../-watcher/index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeferredWatcher(deferred: Deferred<`[`Watcher`](../-watcher/index.md)`>)`<br>Exposes a [deferred](#) as a [Watcher](../-watcher/index.md). |

### Functions

| Name | Summary |
|---|---|
| [cancel](cancel.md) | `open fun cancel(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Immediately stop. Repeated invocations have no effect. |
| [start](start.md) | `open suspend fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Return when the watcher has become effective. |
| [stop](stop.md) | `open suspend fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Gracefully stop, suspending if necessary to allow outstanding operations to complete. Repeated invocations have no effect. |

### Inherited Functions

| Name | Summary |
|---|---|
| [plus](../-watcher/plus.md) | `open operator fun plus(other: `[`Watcher`](../-watcher/index.md)`): `[`Watcher`](../-watcher/index.md)<br>Combine two [Watcher](../-watcher/index.md) objects, returning a single one that spans both. |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |
