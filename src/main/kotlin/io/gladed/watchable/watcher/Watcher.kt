package io.gladed.watchable.watcher

import io.gladed.watchable.Change

/** An object watching for the arrival of changes. */
internal interface Watcher<C : Change> {
    /** Dispatch changes to the watcher, returning false if it should no longer receive any. */
    suspend fun dispatch(changes: List<C>): Boolean
}
