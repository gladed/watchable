[io.gladed.watchable](../index.md) / [MapChange](./index.md)

# MapChange

`sealed class MapChange<K, V> : `[`HasSimpleChange`](../-has-simple-change/index.md)`<`[`MapChange.Simple`](-simple/index.md)`<`[`K`](index.md#K)`, `[`V`](index.md#V)`>>`

Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html).

### Types

| Name | Summary |
|---|---|
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map. |
| [Put](-put/index.md) | `data class Put<K, V> : `[`MapChange`](./index.md)`<`[`K`](-put/index.md#K)`, `[`V`](-put/index.md#V)`>`<br>The addition of value [add](-put/add.md), replacing value [remove](-put/remove.md) if present, for [key](-put/key.md). |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A removal of item [remove](-remove/remove.md) for [key](-remove/key.md). |
| [Simple](-simple/index.md) | `data class Simple<K, V>`<br>The simplified form of a [MapChange](./index.md). |

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
| [Initial](-initial/index.md) | `data class Initial<K, V> : `[`MapChange`](./index.md)`<`[`K`](-initial/index.md#K)`, `[`V`](-initial/index.md#V)`>`<br>The initial state of the map. |
| [Put](-put/index.md) | `data class Put<K, V> : `[`MapChange`](./index.md)`<`[`K`](-put/index.md#K)`, `[`V`](-put/index.md#V)`>`<br>The addition of value [add](-put/add.md), replacing value [remove](-put/remove.md) if present, for [key](-put/key.md). |
| [Remove](-remove/index.md) | `data class Remove<K, V> : `[`MapChange`](./index.md)`<`[`K`](-remove/index.md#K)`, `[`V`](-remove/index.md#V)`>`<br>A removal of item [remove](-remove/remove.md) for [key](-remove/key.md). |
