[io.gladed.watchable](../index.md) / [Change](./index.md)

# Change

`interface Change<out T, out V>`

Describes a change to an object of type [T](index.md#T) which contains values of type [V](index.md#V).

### Properties

| Name | Summary |
|---|---|
| [simple](simple.md) | `abstract val simple: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SimpleChange`](../-simple-change/index.md)`<`[`V`](index.md#V)`>>`<br>A list of [SimpleChange](../-simple-change/index.md) objects that describe all parts of this change. |

### Inheritors

| Name | Summary |
|---|---|
| [GroupChange](../-group-change/index.md) | `data class GroupChange<out T, out V, C : `[`Change`](./index.md)`<`[`T`](../-group-change/index.md#T)`, `[`V`](../-group-change/index.md#V)`>> : `[`Change`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Watchable`](../-watchable/index.md)`<`[`T`](../-group-change/index.md#T)`, `[`V`](../-group-change/index.md#V)`, `[`C`](../-group-change/index.md#C)`>>, `[`Watchable`](../-watchable/index.md)`<`[`T`](../-group-change/index.md#T)`, `[`V`](../-group-change/index.md#V)`, `[`C`](../-group-change/index.md#C)`>>`<br>A single change to a single watchable in a group. |
| [ListChange](../-list-change/index.md) | `sealed class ListChange<T> : `[`Change`](./index.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](../-list-change/index.md#T)`>, `[`T`](../-list-change/index.md#T)`>`<br>Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). |
| [MapChange](../-map-change/index.md) | `sealed class MapChange<K, out V> : `[`Change`](./index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](../-map-change/index.md#K)`, `[`V`](../-map-change/index.md#V)`>, `[`V`](../-map-change/index.md#V)`>`<br>Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [SetChange](../-set-change/index.md) | `sealed class SetChange<T> : `[`Change`](./index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](../-set-change/index.md#T)`>, `[`T`](../-set-change/index.md#T)`>`<br>Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html). |
| [ValueChange](../-value-change/index.md) | `data class ValueChange<T> : `[`Change`](./index.md)`<`[`T`](../-value-change/index.md#T)`, `[`T`](../-value-change/index.md#T)`>`<br>Describes the replacement of a value. |
