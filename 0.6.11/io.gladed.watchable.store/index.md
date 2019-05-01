[io.gladed.watchable.store](./index.md)

## Package io.gladed.watchable.store

### Types

| Name | Summary |
|---|---|
| [Cache](-cache/index.md) | `class Cache<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Store`](-store/index.md)`<`[`T`](-cache/index.md#T)`>, CoroutineScope`<br>A RAM cache that prevents overuse of backing store by serving objects it already has loaded. |
| [FileStore](-file-store/index.md) | `class FileStore : `[`Store`](-store/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Read/write strings to files for each key. |
| [Hold](-hold/index.md) | `interface Hold`<br>Represents an object being held for use with resources or side effects to be managed by this object. |
| [HoldingStore](-holding-store/index.md) | `class HoldingStore<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : CoroutineScope`<br>A [Store](-store/index.md) factory producing stores that trigger operations on its items while those objects are in use. |
| [Inflater](-inflater/index.md) | `interface Inflater<A : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, B : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>Convert between "deflated" ([A](-inflater/index.md#A)) and "inflated" ([B](-inflater/index.md#B)) forms of an object. |
| [MemoryStore](-memory-store/index.md) | `class MemoryStore<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Store`](-store/index.md)`<`[`T`](-memory-store/index.md#T)`>`<br>A store entirely in RAM. |
| [Store](-store/index.md) | `interface Store<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>An object that retrieves elements by key. |

### Exceptions

| Name | Summary |
|---|---|
| [Cannot](-cannot/index.md) | `open class Cannot : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Something cannot be done. |

### Functions

| Name | Summary |
|---|---|
| [cached](cached.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](cached.md#T)`>.cached(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`Cache`](-cache/index.md)`<`[`T`](cached.md#T)`>`<br>Return a memory cached version of this [Store](-store/index.md). |
| [cannot](cannot.md) | `fun cannot(doSomething: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)<br>Throw an exception to complain that something cannot be done. |
| [holding](holding.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](holding.md#T)`>.holding(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, start: suspend (`[`T`](holding.md#T)`) -> `[`Hold`](-hold/index.md)`): `[`HoldingStore`](-holding-store/index.md)`<`[`T`](holding.md#T)`>`<br>Return a [HoldingStore](-holding-store/index.md) around this [Store](-store/index.md). |
| [plus](plus.md) | `operator fun `[`Hold`](-hold/index.md)`.plus(other: `[`Hold`](-hold/index.md)`): `[`Hold`](-hold/index.md)<br>Combine the behaviors of this [Hold](-hold/index.md) object with [other](plus.md#io.gladed.watchable.store$plus(io.gladed.watchable.store.Hold, io.gladed.watchable.store.Hold)/other).`operator fun `[`Hold`](-hold/index.md)`.plus(other: `[`Watcher`](../io.gladed.watchable/-watcher/index.md)`): `[`Hold`](-hold/index.md)<br>Combine the behaviors of this [Hold](-hold/index.md) object with a [Watcher](../io.gladed.watchable/-watcher/index.md). |
