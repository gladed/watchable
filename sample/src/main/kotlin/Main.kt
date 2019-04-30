import external.Adapter
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.Serializable
import model.Bird
import model.MutableBird
import util.KotlinSerializationConverter
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

@UseExperimental(FlowPreview::class)
class Main : CoroutineScope {

    override val coroutineContext = Dispatchers.Default + Job()
    private val logic = Adapter.createLogic(coroutineContext, File("store"))

    fun go() {
        embeddedServer(Netty, SAMPLE_PORT) {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, KotlinSerializationConverter()) {
                    add(Home.serializer())
                    add(CreateBird.serializer())
                    add(Bird.serializer())
                }
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
            val birdRequest = call.receive<CreateBird>()

            val bird = coroutineScope {
                val birds = logic.birds.create(this)
                MutableBird.inflate(Bird(name = birdRequest.name)).also { bird ->
                    birds.put(bird.id, bird)
                }
            }.toImmutable()

            call.respond(bird)
        }
        get("{id}") {
            val bird = coroutineScope {
                logic.birds.create(this).get(call.parameters["id"]!!)
            }.toImmutable()

            call.respond(bird)
        }
    }

    companion object {
        const val SAMPLE_PORT = 8080
        const val SHORT_LIST_COUNT = 10
    }
}
