[io.gladed.watchable](index.md) / [Change](./-change.md)

# Change

`interface Change<T>`

Describes a change to an object of type [T](-change.md#T).

### Inheritors

| Name | Summary |
|---|---|
| [ListChange](-list-change/index.md) | `sealed class ListChange<T> : `[`Change`](./-change.md)`<`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`T`](-list-change/index.md#T)`>>`<br>Describes a change to a [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html). |
| [MapChange](-map-change/index.md) | `sealed class MapChange<K, V> : `[`Change`](./-change.md)`<`[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html)`<`[`K`](-map-change/index.md#K)`, `[`V`](-map-change/index.md#V)`>>`<br>Describes a change to a [Map](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html). |
| [SetChange](-set-change/index.md) | `sealed class SetChange<T> : `[`Change`](./-change.md)`<`[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`T`](-set-change/index.md#T)`>>`<br>Describes a change to a [Set](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html). |
| [ValueChange](-value-change/index.md) | `data class ValueChange<T> : `[`Change`](./-change.md)`<`[`T`](-value-change/index.md#T)`>`<br>An initial announcement or change to the underlying value for a [WatchableValue](-watchable-value/index.md). |
