[io.gladed.watchable](../index.md) / [MutableWatchable](./index.md)

# MutableWatchable

`interface MutableWatchable<T, M : `[`T`](index.md#T)`, C : `[`Change`](../-change.md)`<`[`T`](index.md#T)`>> : `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>`

A [Watchable](../-watchable/index.md) which may be mutated in the form of an [M](index.md#M) and bound to other [Watchable](../-watchable/index.md) sources.

### Properties

| Name | Summary |
|---|---|
| [boundTo](bound-to.md) | `abstract val boundTo: `[`Watchable`](../-watchable/index.md)`<*, *>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |

### Inherited Properties

| Name | Summary |
|---|---|
| [value](../-watchable/value.md) | `abstract val value: `[`T`](../-watchable/index.md#T)<br>Return an immutable copy of the current value of [T](../-watchable/index.md#T). |

### Functions

| Name | Summary |
|---|---|
| [bind](bind.md) | `abstract fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`T`](index.md#T)`, `[`C`](index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.T, io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`abstract fun <T2, C2 : `[`Change`](../-change.md)`<`[`T2`](bind.md#T2)`>> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`T2`](bind.md#T2)`, `[`C2`](bind.md#C2)`>, apply: `[`M`](index.md#M)`.(`[`C2`](bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.T2, io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [isBound](is-bound.md) | `open fun isBound(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Return true if this object is already bound. |
| [set](set.md) | `abstract suspend fun set(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Completely replace the contents of this watchable. |
| [unbind](unbind.md) | `abstract fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Cancel any existing binding that exists for this object. |
| [use](use.md) | `abstract suspend fun <U> use(func: `[`M`](index.md#M)`.() -> `[`U`](use.md#U)`): `[`U`](use.md#U)<br>Suspend until [func](use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](index.md#M) as desired and returning the result. Note: if currently bound ([isBound](is-bound.md) returns true), attempts to modify [M](index.md#M) will throw. |

### Inherited Functions

| Name | Summary |
|---|---|
| [batch](../-watchable/batch.md) | `open fun batch(scope: CoroutineScope, minPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 0, func: suspend (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver lists of changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](../-watchable/batch.md#io.gladed.watchable.Watchable$batch(kotlinx.coroutines.CoroutineScope, kotlin.Long, kotlin.SuspendFunction1((kotlin.collections.List((io.gladed.watchable.Watchable.C)), kotlin.Unit)))/scope) completes. |
| [subscribe](../-watchable/subscribe.md) | `abstract fun subscribe(scope: CoroutineScope): ReceiveChannel<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`C`](../-watchable/index.md#C)`>>`<br>Return a channel which will receive successive lists of changes as they occur. |
| [watch](../-watchable/watch.md) | `open fun watch(scope: CoroutineScope, func: suspend (`[`C`](../-watchable/index.md#C)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`<br>Deliver changes for this [Watchable](../-watchable/index.md) to [func](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/func), starting with its initial state, until the returned [Job](#) is cancelled or the [scope](../-watchable/watch.md#io.gladed.watchable.Watchable$watch(kotlinx.coroutines.CoroutineScope, kotlin.SuspendFunction1((io.gladed.watchable.Watchable.C, kotlin.Unit)))/scope) completes. |

### Inheritors

| Name | Summary |
|---|---|
| [MutableWatchableBase](../-mutable-watchable-base/index.md) | `abstract class MutableWatchableBase<T, M : `[`T`](../-mutable-watchable-base/index.md#T)`, C : `[`Change`](../-change.md)`<`[`T`](../-mutable-watchable-base/index.md#T)`>> : `[`MutableWatchable`](./index.md)`<`[`T`](../-mutable-watchable-base/index.md#T)`, `[`M`](../-mutable-watchable-base/index.md#M)`, `[`C`](../-mutable-watchable-base/index.md#C)`>`<br>Base for implementing a type that is watchable, mutable, and bindable. |
