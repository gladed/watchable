[io.gladed.watchable](../index.md) / [Period](index.md) / [INLINE](./-i-n-l-i-n-e.md)

# INLINE

`const val INLINE: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)

A watcher that runs before the change is fully applied. If it throws the change will be rolled
back and the exception re-thrown at the site of the change.

