import external.Adapter
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.content.TextContent
import io.ktor.features.ContentConverter
import io.ktor.features.ContentNegotiation
import io.ktor.features.suitableCharset
import io.ktor.http.ContentType
import io.ktor.http.withCharset
import io.ktor.request.ApplicationReceiveRequest
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import model.Bird
import model.MutableBird
import java.io.File

fun main() = Main().go()

@Serializable
data class Home(
    val birdsUrl: String = "/bird",
    val someBirdUrls: List<String>
)

@Serializable
data class CreateBird(
    val name: String
)

@UseExperimental(FlowPreview::class, UnstableDefault::class, KtorExperimentalAPI::class)
class Main : CoroutineScope {

    override val coroutineContext = Dispatchers.Default + Job()
    private val logic = Adapter.createLogic(coroutineContext, File("store"))

    fun go() {
        embeddedServer(Netty, SAMPLE_PORT) {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, HomeConverter())
            }
            routing {
                get("/") {
                    call.respond(Home(someBirdUrls = logic.birds.back.keys()
                        .take(SHORT_LIST_COUNT).toList().map { "/bird/$it" }))
                }
                route("bird") { birdRoutes() }
            }
        }.start(wait = true)
    }

    private fun Route.birdRoutes() {
        post {
            coroutineScope {
                val birdRequest = call.receive<CreateBird>()
                val birds = logic.birds.create(this)
                val bird = MutableBird.inflate(Bird(name = birdRequest.name))
                birds.put(bird.id, bird)
                call.respond(bird)
            }
        }
        get("{id}") {
            coroutineScope {
                val bird = logic.birds.create(this).get(call.parameters["id"]!!)
                call.respond(bird)
            }
        }
    }

    class HomeConverter : ContentConverter {
        override suspend fun convertForReceive(
            context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>
        ): Any? {
            val request = context.subject
            return when (request.type.javaObjectType) {
                CreateBird::class.java -> Json.parse(
                    CreateBird.serializer(),
                    (request.value as? ByteReadChannel ?: return null).readRemaining(MAX_REQUEST_SIZE, 0).readText())
                else -> null
            }
        }
        override suspend fun convertForSend(
            context: PipelineContext<Any, ApplicationCall>,
            contentType: ContentType,
            value: Any
        ): Any? {
            return when (value) {
                is Home ->
                    TextContent(Json.stringify(Home.serializer(), value),
                        contentType.withCharset(context.call.suitableCharset()))
                is MutableBird ->
                    TextContent(Json.stringify(Bird.serializer(), MutableBird.deflate(value)),
                        contentType.withCharset(context.call.suitableCharset()))
                else -> null
            }
        }
    }

    companion object {
        const val SAMPLE_PORT = 8080
        const val MAX_REQUEST_SIZE = 1024 * 10L
        const val SHORT_LIST_COUNT = 10
    }
}
