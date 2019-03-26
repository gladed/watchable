[io.gladed.watchable](../index.md) / [SetChange](./index.md)

# SetChange

`sealed class SetChange<T> : `[`Change`](../-change/index.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](index.md#T)`>, `[`T`](index.md#T)`>`

Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html).

### Types

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>`<br>A change representing the addition of an element to the set. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`SetChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the set at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A change representing the removal of an element from the set. |

### Inherited Properties

| Name | Summary |
|---|---|
| [simple](../-change/simple.md) | `abstract val simple: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SimpleChange`](../-simple-change/index.md)`<`[`V`](../-change/index.md#V)`>>`<br>A list of [SimpleChange](../-simple-change/index.md) objects that describe all parts of this change. |

### Inheritors

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`SetChange`](./index.md)`<`[`T`](-add/index.md#T)`>`<br>A change representing the addition of an element to the set. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`SetChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the set at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`SetChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A change representing the removal of an element from the set. |
