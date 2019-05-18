[io.gladed.watchable.store](./index.md)

## Package io.gladed.watchable.store

### Types

| Name | Summary |
|---|---|
| [Cache](-cache/index.md) | `class Cache<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Store`](-store/index.md)`<`[`T`](-cache/index.md#T)`>, CoroutineScope`<br>A RAM cache that prevents overuse of backing store by serving objects it already has loaded. |
| [Container](-container/index.md) | `interface Container`<br>An object containing a watchable representing changes for a whole object. |
| [FileStore](-file-store/index.md) | `class FileStore : `[`Store`](-store/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Read/write strings to files for each key. |
| [Hold](-hold/index.md) | `interface Hold`<br>Represents resources held on behalf of an object. |
| [HoldingStore](-holding-store/index.md) | `class HoldingStore<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : CoroutineScope`<br>A [Store](-store/index.md) factory producing stores that trigger operations on its items while those objects are in use. |
| [MemoryStore](-memory-store/index.md) | `class MemoryStore<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Store`](-store/index.md)`<`[`T`](-memory-store/index.md#T)`>`<br>A store entirely in RAM. |
| [Store](-store/index.md) | `interface Store<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>An object that retrieves elements by a String key. |
| [Transformer](-transformer/index.md) | `interface Transformer<S : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>Convert between source ([S](-transformer/index.md#S)) and target ([T](-transformer/index.md#T)) forms of an object. |

### Exceptions

| Name | Summary |
|---|---|
| [Cannot](-cannot/index.md) | `open class Cannot : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Something cannot be done. |

### Properties

| Name | Summary |
|---|---|
| [DEFAULT_CONTAINER_PERIOD](-d-e-f-a-u-l-t_-c-o-n-t-a-i-n-e-r_-p-e-r-i-o-d.md) | `const val DEFAULT_CONTAINER_PERIOD: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |

### Functions

| Name | Summary |
|---|---|
| [bind](bind.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](bind.md#T)`>.bind(scope: CoroutineScope, period: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, map: `[`WatchableMap`](../io.gladed.watchable/-watchable-map/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, `[`T`](bind.md#T)`>): `[`Watcher`](../io.gladed.watchable/-watcher/index.md)<br>Load up a [WatchableMap](../io.gladed.watchable/-watchable-map/index.md) with all items in this [Store](-store/index.md), and persisting changes from the map to the store as they happen until [scope](bind.md#io.gladed.watchable.store$bind(io.gladed.watchable.store.Store((io.gladed.watchable.store.bind.T)), kotlinx.coroutines.CoroutineScope, kotlin.Long, io.gladed.watchable.WatchableMap((kotlin.String, io.gladed.watchable.store.bind.T)))/scope) completes. |
| [cached](cached.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](cached.md#T)`>.cached(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`Cache`](-cache/index.md)`<`[`T`](cached.md#T)`>`<br>Return a memory cached version of this [Store](-store/index.md). |
| [cannot](cannot.md) | `fun cannot(doSomething: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)<br>Throw an exception to complain that something cannot be done. |
| [holding](holding.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](holding.md#T)`>.holding(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, containerPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = DEFAULT_CONTAINER_PERIOD, start: suspend (`[`T`](holding.md#T)`) -> `[`Hold`](-hold/index.md)`): `[`HoldingStore`](-holding-store/index.md)`<`[`T`](holding.md#T)`>`<br>Return a [HoldingStore](-holding-store/index.md) around this [Store](-store/index.md). |
| [plus](plus.md) | `operator fun `[`Hold`](-hold/index.md)`.plus(other: `[`Hold`](-hold/index.md)`): `[`Hold`](-hold/index.md)<br>Combine the behaviors of this [Hold](-hold/index.md) object with [other](plus.md#io.gladed.watchable.store$plus(io.gladed.watchable.store.Hold, io.gladed.watchable.store.Hold)/other).`operator fun `[`Hold`](-hold/index.md)`.plus(other: `[`Watcher`](../io.gladed.watchable/-watcher/index.md)`): `[`Hold`](-hold/index.md)<br>Combine the behaviors of this [Hold](-hold/index.md) object with a [Watcher](../io.gladed.watchable/-watcher/index.md). |
| [toHold](to-hold.md) | `fun `[`Watcher`](../io.gladed.watchable/-watcher/index.md)`.toHold(): `[`Hold`](-hold/index.md)<br>Return a [Hold](-hold/index.md) form of this [Watcher](../io.gladed.watchable/-watcher/index.md). |
| [transform](transform.md) | `fun <U : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](transform.md#T)`>.transform(transformer: `[`Transformer`](-transformer/index.md)`<`[`T`](transform.md#T)`, `[`U`](transform.md#U)`>): `[`Store`](-store/index.md)`<`[`U`](transform.md#U)`>`<br>Expose this [Store](-store/index.md) of [T](transform.md#T) items as a [Store](-store/index.md) of transformed items [U](transform.md#U). |
