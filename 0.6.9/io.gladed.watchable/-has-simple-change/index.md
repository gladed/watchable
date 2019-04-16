[io.gladed.watchable](../index.md) / [HasSimpleChange](./index.md)

# HasSimpleChange

`interface HasSimpleChange<out S> : `[`Change`](../-change.md)

A [Change](../-change.md) that can be expressed in terms of simpler change objects of type [S](index.md#S).

### Properties

| Name | Summary |
|---|---|
| [simple](simple.md) | `abstract val simple: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`S`](index.md#S)`>` |

### Extension Functions

| Name | Summary |
|---|---|
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [toWatchableValue](../to-watchable-value.md) | `fun <T> `[`T`](../to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../-watchable-value/index.md)`<`[`T`](../to-watchable-value.md#T)`>`<br>Convert this [T](../to-watchable-value.md#T) to a watchable value of [T](../to-watchable-value.md#T). |

### Inheritors

| Name | Summary |
|---|---|
| [ListChange](../-list-change/index.md) | `sealed class ListChange<T> : `[`HasSimpleChange`](./index.md)`<`[`ListChange.Simple`](../-list-change/-simple/index.md)`<`[`T`](../-list-change/index.md#T)`>>`<br>Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). |
| [MapChange](../-map-change/index.md) | `sealed class MapChange<K, V> : `[`HasSimpleChange`](./index.md)`<`[`MapChange.Simple`](../-map-change/-simple/index.md)`<`[`K`](../-map-change/index.md#K)`, `[`V`](../-map-change/index.md#V)`>>`<br>Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [SetChange](../-set-change/index.md) | `sealed class SetChange<T> : `[`HasSimpleChange`](./index.md)`<`[`SetChange.Simple`](../-set-change/-simple/index.md)`<`[`T`](../-set-change/index.md#T)`>>`<br>Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html). |
