package io.gladed.watchable

/**
 * Defines special values for watcher timing.
 *
 * When period is >0, changes are collected and delivered no more frequently than that many milliseconds.
 */
object Period {
    /**
     * A watcher with this period run very soon after the change is made. This is the default for all
     * watching operations.
     */
    const val IMMEDIATE = 0L

    /**
     * A watcher that runs before the change is fully applied. If it throws the change will be rolled
     * back and the exception re-thrown at the site of the change.
     */
    const val INLINE = -1L
}