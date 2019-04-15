[io.gladed.watchable](../index.md) / [WatchableList](index.md) / [plusAssign](./plus-assign.md)

# plusAssign

`suspend inline operator fun plusAssign(element: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Add [element](plus-assign.md#io.gladed.watchable.WatchableList$plusAssign(io.gladed.watchable.WatchableList.T)/element) to this watchable collection.

`suspend inline operator fun plusAssign(elements: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Adds all elements of the given [elements](plus-assign.md#io.gladed.watchable.WatchableList$plusAssign(kotlin.collections.Iterable((io.gladed.watchable.WatchableList.T)))/elements) collection to this watchable collection.

`suspend inline operator fun plusAssign(elements: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)
`suspend inline operator fun plusAssign(elements: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`T`](index.md#T)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Adds all elements of the given [elements](plus-assign.md#io.gladed.watchable.WatchableList$plusAssign(kotlin.Array((io.gladed.watchable.WatchableList.T)))/elements) collection to this list.

