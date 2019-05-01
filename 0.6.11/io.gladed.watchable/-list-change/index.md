[io.gladed.watchable](../index.md) / [ListChange](./index.md)

# ListChange

`sealed class ListChange<T> : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`ListChange.Simple`](-simple/index.md)`<`[`T`](index.md#T)`>>`

Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html).

### Types

| Name | Summary |
|---|---|
| [Initial](-initial/index.md) | `data class Initial<T> : `[`ListChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the list, delivered upon first watch. |
| [Insert](-insert/index.md) | `data class Insert<T> : `[`ListChange`](./index.md)`<`[`T`](-insert/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`ListChange`](./index.md)`<`[`T`](-insert/index.md#T)`>>`<br>An insertion of items at [index](-insert/--index--.md). |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`ListChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A removal of [remove](-remove/remove.md) at [index](-remove/--index--.md). |
| [Replace](-replace/index.md) | `data class Replace<T> : `[`ListChange`](./index.md)`<`[`T`](-replace/index.md#T)`>`<br>An overwriting of [remove](-replace/remove.md) at [index](-replace/--index--.md), replacing with [add](-replace/add.md). |
| [Simple](-simple/index.md) | `data class Simple<T>`<br>The simplest form of a list change, affecting only a single position in the list. |

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
| [Initial](-initial/index.md) | `data class Initial<T> : `[`ListChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the list, delivered upon first watch. |
| [Insert](-insert/index.md) | `data class Insert<T> : `[`ListChange`](./index.md)`<`[`T`](-insert/index.md#T)`>, `[`Addable`](../-addable/index.md)`<`[`ListChange`](./index.md)`<`[`T`](-insert/index.md#T)`>>`<br>An insertion of items at [index](-insert/--index--.md). |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`ListChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A removal of [remove](-remove/remove.md) at [index](-remove/--index--.md). |
| [Replace](-replace/index.md) | `data class Replace<T> : `[`ListChange`](./index.md)`<`[`T`](-replace/index.md#T)`>`<br>An overwriting of [remove](-replace/remove.md) at [index](-replace/--index--.md), replacing with [add](-replace/add.md). |
