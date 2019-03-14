[io.gladed.watchable](../index.md) / [kotlinx.coroutines.CoroutineScope](index.md) / [daemon](./daemon.md)

# daemon

`fun CoroutineScope.daemon(context: `[`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)` = EmptyCoroutineContext, block: suspend CoroutineScope.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): Job`

Similar to [launch](#) but does not block parent's ability to [Job.join](#). Cancels when parent cancels.

