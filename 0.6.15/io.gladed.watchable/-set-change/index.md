[io.gladed.watchable](../index.md) / [SetChange](./index.md)

# SetChange

`sealed class SetChange<T> : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`SetChange.Simple`](-simple/index.md)`<`[`T`](index.md#T)`>>`

Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html).

### Types

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>>`<br>An addition of items. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`SetChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the set. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>>`<br>A removal of items. |
| [Simple](-simple/index.md) | `data class Simple<T>`<br>The simplest form of a set change, affecting only a single item (either [add](-simple/add.md) or [remove](-simple/remove.md)). |

### Properties

| Name | Summary |
|---|---|
| [isInitial](is-initial.md) | `open val isInitial: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>True when this change represents the initial state of a watched object. |

### Inherited Properties

| Name | Summary |
|---|---|
| [simple](../-has-simple-change/simple.md) | `abstract val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`S`](../-has-simple-change/index.md#S)`>` |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>>`<br>An addition of items. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`SetChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the set. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>>`<br>A removal of items. |