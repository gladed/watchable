package store

import io.gladed.watchable.util.Stoppable

interface Removable : Stoppable {
    suspend fun remove()
}
