[io.gladed.watchable](../index.md) / [WatchableSet](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`WatchableSet(coroutineContext: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)`, elements: `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`T`](index.md#T)`> = emptyList())`

A mutable set whose contents may be watched for changes and/or bound to other maps for the duration
of its [coroutineContext](coroutine-context.md). Insertion order is preserved on iteration.

