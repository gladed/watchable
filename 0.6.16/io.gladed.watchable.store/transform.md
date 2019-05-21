[io.gladed.watchable.store](index.md) / [transform](./transform.md)

# transform

`fun <U : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`Store`](-store/index.md)`<`[`T`](transform.md#T)`>.transform(transformer: `[`Transformer`](-transformer/index.md)`<`[`T`](transform.md#T)`, `[`U`](transform.md#U)`>): `[`Store`](-store/index.md)`<`[`U`](transform.md#U)`>`

Expose this [Store](-store/index.md) of [T](transform.md#T) items as a [Store](-store/index.md) of transformed items [U](transform.md#U).

