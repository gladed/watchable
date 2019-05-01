[io.gladed.watchable.store](../index.md) / [MemoryStore](./index.md)

# MemoryStore

`class MemoryStore<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Store`](../-store/index.md)`<`[`T`](index.md#T)`>`

A store entirely in RAM.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `MemoryStore(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>A store entirely in RAM. |

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `suspend fun delete(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Delete any data found at [key](../-store/delete.md#io.gladed.watchable.store.Store$delete(kotlin.String)/key). |
| [get](get.md) | `suspend fun get(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`T`](index.md#T)<br>Return the corresponding element, or throw if not present. |
| [keys](keys.md) | `fun keys(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Return the keys available in memory at the moment the flow is first consumed. |
| [put](put.md) | `suspend fun put(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Write something to the store at the given key, overwriting what was there, if anything. |

### Inherited Functions

| Name | Summary |
|---|---|
| [inflate](../-store/inflate.md) | `open fun <U : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> inflate(inflater: `[`Inflater`](../-inflater/index.md)`<`[`T`](../-store/index.md#T)`, `[`U`](../-store/inflate.md#U)`>): `[`Store`](../-store/index.md)`<`[`U`](../-store/inflate.md#U)`>`<br>Convert this [Store](../-store/index.md) of deflated items into a [Store](../-store/index.md) of inflated items [U](../-store/inflate.md#U). |

### Extension Functions

| Name | Summary |
|---|---|
| [cached](../cached.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](../-store/index.md)`<`[`T`](../cached.md#T)`>.cached(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`Cache`](../-cache/index.md)`<`[`T`](../cached.md#T)`>`<br>Return a memory cached version of this [Store](../-store/index.md). |
| [guarded](../../io.gladed.watchable.util/guarded.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guarded.md#T)`.guarded(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guarded.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guarded.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [holding](../holding.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](../-store/index.md)`<`[`T`](../holding.md#T)`>.holding(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, start: suspend (`[`T`](../holding.md#T)`) -> `[`Hold`](../-hold/index.md)`): `[`HoldingStore`](../-holding-store/index.md)`<`[`T`](../holding.md#T)`>`<br>Return a [HoldingStore](../-holding-store/index.md) around this [Store](../-store/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
