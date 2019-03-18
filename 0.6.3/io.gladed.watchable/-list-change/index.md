[io.gladed.watchable](../index.md) / [ListChange](./index.md)

# ListChange

`sealed class ListChange<T> : `[`Change`](../-change.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](index.md#T)`>>`

Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html).

### Types

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`ListChange`](./index.md)`<`[`T`](-add/index.md#T)`>`<br>An addition of an element to the list. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`ListChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the list at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`ListChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A removal of an element in the list. |
| [Replace](-replace/index.md) | `data class Replace<T> : `[`ListChange`](./index.md)`<`[`T`](-replace/index.md#T)`>`<br>A replacement of the element at a specific place in the list. |

### Inheritors

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<T> : `[`ListChange`](./index.md)`<`[`T`](-add/index.md#T)`>`<br>An addition of an element to the list. |
| [Initial](-initial/index.md) | `data class Initial<T> : `[`ListChange`](./index.md)`<`[`T`](-initial/index.md#T)`>`<br>The initial state of the list at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<T> : `[`ListChange`](./index.md)`<`[`T`](-remove/index.md#T)`>`<br>A removal of an element in the list. |
| [Replace](-replace/index.md) | `data class Replace<T> : `[`ListChange`](./index.md)`<`[`T`](-replace/index.md#T)`>`<br>A replacement of the element at a specific place in the list. |
