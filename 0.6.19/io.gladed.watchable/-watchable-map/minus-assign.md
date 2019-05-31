[io.gladed.watchable](../index.md) / [WatchableMap](index.md) / [minusAssign](./minus-assign.md)

# minusAssign

`open suspend operator fun minusAssign(key: `[`K`](index.md#K)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Remove any value corresponding to [key](minus-assign.md#io.gladed.watchable.WatchableMap$minusAssign(io.gladed.watchable.WatchableMap.K)/key) from this map.

`open suspend operator fun minusAssign(removeKeys: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`K`](index.md#K)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)
`open suspend operator fun minusAssign(removeKeys: `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`K`](index.md#K)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)
`open suspend operator fun minusAssign(removeKeys: `[`Sequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.sequences/-sequence/index.html)`<`[`K`](index.md#K)`>): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)

Remove [removeKeys](minus-assign.md#io.gladed.watchable.WatchableMap$minusAssign(kotlin.Array((io.gladed.watchable.WatchableMap.K)))/removeKeys) from this map, along with any associated values.

