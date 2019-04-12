package io.gladed.watchable

/** An ongoing operation that can be closed or cancelled. */
interface Busy {

    /** Immediately stop. Repeated invocations have no effect. */
    fun cancel()

    /**
     * Gracefully stop, suspending if necessary to allow underlying operations to complete.
     * Repeated invocations have no effect.
     */
    suspend fun close()

    /** Combine two [Busy] objects, returning a single one that spans both. */
    operator fun plus(right: Busy) = object : Busy {
        override fun cancel() {
            this@Busy.cancel()
            right.cancel()
        }

        override suspend fun close() {
            this@Busy.close()
            right.close()
        }
    }
}