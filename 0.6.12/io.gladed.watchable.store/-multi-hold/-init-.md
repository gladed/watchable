[io.gladed.watchable.store](../index.md) / [MultiHold](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`MultiHold(entity: `[`E`](index.md#E)`, value: `[`T`](index.md#T)`, hold: `[`Hold`](../-hold/index.md)`)`

Construct a non-deferred version of this [MultiHold](index.md).

`MultiHold(entity: `[`E`](index.md#E)`, hold: Deferred<`[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`T`](index.md#T)`, `[`Hold`](../-hold/index.md)`>>)`

A deferred attempt to acquire an instance of [T](index.md#T) on behalf of one or more holding entities,
starting with [entity](#).

