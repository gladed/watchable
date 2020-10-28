[io.gladed.watchable](../index.md) / [WatchableSet](./index.md)

# WatchableSet

`class WatchableSet<T> : `[`MutableWatchableBase`](../-mutable-watchable-base/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>, `[`T`](index.md#T)`, `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](index.md#T)`>, `[`SetChange`](../-set-change/index.md)`<`[`T`](index.md#T)`>>, `[`ReadOnlyWatchableSet`](../-read-only-watchable-set.md)`<`[`T`](index.md#T)`>`

A [Watchable](../-watchable/index.md) wrapper for a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html) which may also be modified or bound. Use [watchableSetOf](../watchable-set-of.md) to create.

### Properties

| Name | Summary |
|---|---|
| [immutable](immutable.md) | `var immutable: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>`<br>The current immutable [T](../-mutable-watchable-base/index.md#T) form of [M](../-mutable-watchable-base/index.md#M). |
| [mutable](mutable.md) | `val mutable: `[`AbstractMutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-abstract-mutable-set/index.html)`<`[`T`](index.md#T)`>`<br>The underlying mutable form of the data this object. When changes are applied, [changes](#) must be updated. |
| [size](size.md) | `val size: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Inherited Properties

| Name | Summary |
|---|---|
| [boundTo](../-mutable-watchable-base/bound-to.md) | `open val boundTo: `[`Watchable`](../-watchable/index.md)`<*>?`<br>The [Watchable](../-watchable/index.md) to which this object is bound, if any. |

### Functions

| Name | Summary |
|---|---|
| [add](add.md) | `suspend fun add(value: `[`T`](index.md#T)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Add a [value](add.md#io.gladed.watchable.WatchableSet$add(io.gladed.watchable.WatchableSet.T)/value) to the end of this set, returning true if the set was changed. |
| [addAll](add-all.md) | `suspend fun addAll(elements: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Add all elements of the collection to the end of this set, returning true if the set was changed.`suspend fun addAll(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Add all elements of the iterable to the end of this set, returning true if the set was changed.`suspend fun addAll(elements: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Add all elements of the array to the end of this set, returning true if the set was changed.`suspend fun addAll(elements: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Add all elements of the sequence to the end of this set, returning true if the set was changed. |
| [applyBoundChange](apply-bound-change.md) | `fun `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](index.md#T)`>.applyBoundChange(change: `[`SetChange`](../-set-change/index.md)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Apply [change](../-mutable-watchable-base/apply-bound-change.md#io.gladed.watchable.MutableWatchableBase$applyBoundChange(io.gladed.watchable.MutableWatchableBase.M, io.gladed.watchable.MutableWatchableBase.C)/change) to [M](../-mutable-watchable-base/index.md#M). |
| [clear](clear.md) | `suspend fun clear(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Clear all values from this set. |
| [contains](contains.md) | `fun contains(element: `[`T`](index.md#T)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](contains-all.md) | `fun containsAll(elements: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [equals](equals.md) | `fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | `fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](is-empty.md) | `fun isEmpty(): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](iterator.md) | `fun iterator(): `[`Iterator`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)`<`[`T`](index.md#T)`>` |
| [minusAssign](minus-assign.md) | `suspend operator fun minusAssign(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun minusAssign(elements: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun minusAssign(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun minusAssign(elements: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [plusAssign](plus-assign.md) | `suspend operator fun plusAssign(value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun plusAssign(elements: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun plusAssign(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`suspend operator fun plusAssign(elements: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [readOnly](read-only.md) | `fun readOnly(): `[`ReadOnlyWatchableSet`](../-read-only-watchable-set.md)`<`[`T`](index.md#T)`>`<br>Return an unmodifiable form of this [WatchableSet](./index.md). |
| [remove](remove.md) | `suspend fun remove(value: `[`T`](index.md#T)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Remove [value](remove.md#io.gladed.watchable.WatchableSet$remove(io.gladed.watchable.WatchableSet.T)/value) from this set, returning true if it was present and false if it was not. |
| [removeAll](remove-all.md) | `suspend fun removeAll(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>`suspend fun removeAll(elements: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>`suspend fun removeAll(elements: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Remove all matching elements in the collection from the set, returning true if the set was changed. |
| [retainAll](retain-all.md) | `suspend fun retainAll(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Retains only the elements in this set that are found in the collection, returning true if the set was changed. |
| [toImmutable](to-immutable.md) | `fun `[`MutableSet`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-set/index.html)`<`[`T`](index.md#T)`>.toImmutable(): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>`<br>Copy a mutable [M](../-mutable-watchable-base/index.md#M) to an immutable [T](../-mutable-watchable-base/index.md#T). |
| [toInitialChange](to-initial-change.md) | `fun `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>.toInitialChange(): `[`SetChange.Initial`](../-set-change/-initial/index.md)`<`[`T`](index.md#T)`>?`<br>Given the current state [T](../-mutable-watchable-base/index.md#T) return [C](../-mutable-watchable-base/index.md#C) representing the initial state, if any. |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [bind](../-mutable-watchable-base/bind.md) | `open suspend fun bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C`](../-mutable-watchable-base/index.md#C)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin), such that when [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) changes, this object is updated to match [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/origin) exactly, until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.C)))/scope) completes. While bound, this object may not be externally modified or rebound.`open suspend fun <C2 : `[`Change`](../-change.md)`> bind(scope: CoroutineScope, origin: `[`Watchable`](../-watchable/index.md)`<`[`C2`](../-mutable-watchable-base/bind.md#C2)`>, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, apply: `[`M`](../-mutable-watchable-base/index.md#M)`.(`[`C2`](../-mutable-watchable-base/bind.md#C2)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Binds this unbound object to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), such that for every change to [origin](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/origin), the change is applied to this object with [apply](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/apply), until [scope](../-mutable-watchable/bind.md#io.gladed.watchable.MutableWatchable$bind(kotlinx.coroutines.CoroutineScope, io.gladed.watchable.Watchable((io.gladed.watchable.MutableWatchable.bind.C2)), kotlin.Long, kotlin.Function2((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.bind.C2, kotlin.Unit)))/scope) completes. While bound, this object may not be externally modified or rebound. |
| [doChange](../-mutable-watchable-base/do-change.md) | `fun <U> doChange(func: () -> `[`U`](../-mutable-watchable-base/do-change.md#U)`): `[`U`](../-mutable-watchable-base/do-change.md#U)<br>Run [func](../-mutable-watchable-base/do-change.md#io.gladed.watchable.MutableWatchableBase$doChange(kotlin.Function0((io.gladed.watchable.MutableWatchableBase.doChange.U)))/func) if changes are currently allowed, or throw if not. |
| [getInitialChange](../-mutable-watchable-base/get-initial-change.md) | `open fun getInitialChange(): `[`C`](../-mutable-watchable-base/index.md#C)`?`<br>Return the initial change that a new watcher should receive, if any |
| [record](../-mutable-watchable-base/record.md) | `fun record(change: `[`C`](../-mutable-watchable-base/index.md#C)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Record the latest change. |
| [unbind](../-mutable-watchable-base/unbind.md) | `open fun unbind(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove any existing binding for this object. |
| [use](../-mutable-watchable-base/use.md) | `open suspend fun <U> use(func: `[`M`](../-mutable-watchable-base/index.md#M)`.() -> `[`U`](../-mutable-watchable-base/use.md#U)`): `[`U`](../-mutable-watchable-base/use.md#U)<br>Suspend until [func](../-mutable-watchable/use.md#io.gladed.watchable.MutableWatchable$use(kotlin.Function1((io.gladed.watchable.MutableWatchable.M, io.gladed.watchable.MutableWatchable.use.U)))/func) can safely execute, reading and/or writing data on [M](../-mutable-watchable/index.md#M) as desired and returning the result. Note: if currently bound ([isBound](../-mutable-watchable/is-bound.md) returns true), attempts to modify [M](../-mutable-watchable/index.md#M) will throw. |

### Extension Functions

| Name | Summary |
|---|---|
| [toWatchableList](../kotlin.collections.-collection/to-watchable-list.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](../kotlin.collections.-collection/to-watchable-list.md#T)`>.toWatchableList(): `[`WatchableList`](../-watchable-list/index.md)`<`[`T`](../kotlin.collections.-collection/to-watchable-list.md#T)`>`<br>Return a new [WatchableList](../-watchable-list/index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableSet](../kotlin.collections.-collection/to-watchable-set.md) | `fun <T> `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](../kotlin.collections.-collection/to-watchable-set.md#T)`>.toWatchableSet(): `[`WatchableSet`](./index.md)`<`[`T`](../kotlin.collections.-collection/to-watchable-set.md#T)`>`<br>Return a new [WatchableSet](./index.md) containing the elements of this [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |