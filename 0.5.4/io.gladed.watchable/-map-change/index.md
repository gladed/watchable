[io.gladed.watchable](../index.md) / [MapChange](./index.md)

# MapChange

`sealed class MapChange<K, V> : `[`Change`](../-change.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>`

Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html).

### Types

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<K, V> : `[`MapChange`](./index.md)`<`[`K`](-add/index.md#K)`, `[`V`](-add/index.md#V)`>`<br>A change representing the addition of a new value for a key in the map. |
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A change representing the removal of an element for a key in the map. |
| [Replace](-replace/index.md) | `data class Replace<K, V> : `[`MapChange`](./index.md)`<`[`K`](-replace/index.md#K)`, `[`V`](-replace/index.md#V)`>`<br>A change representing the replacement of an element for a key in the map. |

### Inheritors

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<K, V> : `[`MapChange`](./index.md)`<`[`K`](-add/index.md#K)`, `[`V`](-add/index.md#V)`>`<br>A change representing the addition of a new value for a key in the map. |
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A change representing the removal of an element for a key in the map. |
| [Replace](-replace/index.md) | `data class Replace<K, V> : `[`MapChange`](./index.md)`<`[`K`](-replace/index.md#K)`, `[`V`](-replace/index.md#V)`>`<br>A change representing the replacement of an element for a key in the map. |
