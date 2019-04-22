package external

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import logic.Logic
import model.Bird
import model.Chirp
import model.Operations
import store.FileStore
import util.inflate
import java.io.File
import kotlin.coroutines.CoroutineContext

/** Implement a Logic object backed by real adapters. */
object Adapter {

    @UseExperimental(FlowPreview::class)
    @Suppress("UNUSED_PARAMETER")
    class Ops(root: File) : Operations {
        override fun chirpsForBird(birdId: String): Flow<String> {
            TODO()
        }
    }

    fun createLogic(context: CoroutineContext, root: File): Logic {
        return Logic(context,
            FileStore(root, "bird", ".json").inflate(Bird.serializer()),
            FileStore(root, "chirp", ".json").inflate(Chirp.serializer()),
            Ops(root))
    }
}
