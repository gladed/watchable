[io.gladed.watchable](../index.md) / [MapChange](./index.md)

# MapChange

`sealed class MapChange<K, out V> : `[`Change`](../-change/index.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>, `[`V`](index.md#V)`>`

Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html).

### Types

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<K, V> : `[`MapChange`](./index.md)`<`[`K`](-add/index.md#K)`, `[`V`](-add/index.md#V)`>`<br>A change representing the addition of a new value for a key in the map. |
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A change representing the removal of an element for a key in the map. |
| [Replace](-replace/index.md) | `data class Replace<K, V> : `[`MapChange`](./index.md)`<`[`K`](-replace/index.md#K)`, `[`V`](-replace/index.md#V)`>`<br>A change representing the replacement of an element for a key in the map. |

### Inherited Properties

| Name | Summary |
|---|---|
| [simple](../-change/simple.md) | `abstract val simple: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`SimpleChange`](../-simple-change/index.md)`<`[`V`](../-change/index.md#V)`>>`<br>A list of [SimpleChange](../-simple-change/index.md) objects that describe all parts of this change. |

### Inheritors

| Name | Summary |
|---|---|
| [Add](-add/index.md) | `data class Add<K, V> : `[`MapChange`](./index.md)`<`[`K`](-add/index.md#K)`, `[`V`](-add/index.md#V)`>`<br>A change representing the addition of a new value for a key in the map. |
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map at the time watching began. |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A change representing the removal of an element for a key in the map. |
| [Replace](-replace/index.md) | `data class Replace<K, V> : `[`MapChange`](./index.md)`<`[`K`](-replace/index.md#K)`, `[`V`](-replace/index.md#V)`>`<br>A change representing the replacement of an element for a key in the map. |
