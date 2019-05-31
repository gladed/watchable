[io.gladed.watchable.store](../index.md) / [FileStore](./index.md)

# FileStore

`class FileStore : `[`Store`](../-store/index.md)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`

Read/write strings to files for each key.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `FileStore(rootDir: `[`File`](https://docs.oracle.com/javase/6/docs/api/java/io/File.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, suffix: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "txt")`<br>Read/write strings to files for each key. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `suspend fun get(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Return the corresponding element, or throw if not present. |
| [keys](keys.md) | `fun keys(): Flow<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>Return a flow of all keys present in the store. |
| [put](put.md) | `suspend fun put(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Write something to the store at the given key, overwriting what was there, if anything. |
| [remove](remove.md) | `suspend fun remove(key: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Delete any data found at [key](../-store/remove.md#io.gladed.watchable.store.Store$remove(kotlin.String)/key). |

### Extension Functions

| Name | Summary |
|---|---|
| [cached](../cached.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](../-store/index.md)`<`[`T`](../cached.md#T)`>.cached(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`): `[`Cache`](../-cache/index.md)`<`[`T`](../cached.md#T)`>`<br>Return a memory cached version of this [Store](../-store/index.md). |
| [guard](../../io.gladed.watchable.util/guard.md) | `fun <T> `[`T`](../../io.gladed.watchable.util/guard.md#T)`.guard(): `[`Guard`](../../io.gladed.watchable.util/-guard/index.md)`<`[`T`](../../io.gladed.watchable.util/guard.md#T)`>`<br>Return [T](../../io.gladed.watchable.util/guard.md#T) surrounded by a [Guard](../../io.gladed.watchable.util/-guard/index.md). |
| [holding](../holding.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](../-store/index.md)`<`[`T`](../holding.md#T)`>.holding(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, containerPeriod: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = DEFAULT_CONTAINER_PERIOD, start: suspend `[`HoldBuilder`](../-hold-builder/index.md)`.(`[`T`](../holding.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`HoldingStore`](../-holding-store/index.md)`<`[`T`](../holding.md#T)`>`<br>Return a [HoldingStore](../-holding-store/index.md) around this [Store](../-store/index.md). |
| [toWatchableValue](../../io.gladed.watchable/to-watchable-value.md) | `fun <T> `[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`.toWatchableValue(): `[`WatchableValue`](../../io.gladed.watchable/-watchable-value/index.md)`<`[`T`](../../io.gladed.watchable/to-watchable-value.md#T)`>`<br>Convert this [T](../../io.gladed.watchable/to-watchable-value.md#T) to a watchable value of [T](../../io.gladed.watchable/to-watchable-value.md#T). |
| [transform](../transform.md) | `fun <U : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](../-store/index.md)`<`[`T`](../transform.md#T)`>.transform(transformer: `[`Transformer`](../-transformer/index.md)`<`[`T`](../transform.md#T)`, `[`U`](../transform.md#U)`>): `[`Store`](../-store/index.md)`<`[`U`](../transform.md#U)`>`<br>Expose this [Store](../-store/index.md) of [T](../transform.md#T) items as a [Store](../-store/index.md) of transformed items [U](../transform.md#U). |
