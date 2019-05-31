import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import io.ktor.util.pipeline.ContextDsl
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import rest.Bird
import rest.Chirp
import rest.ChirpReact
import rest.CreateBird
import rest.CreateChirp
import rest.Home
import kotlin.test.assertEquals

@UseExperimental(UnstableDefault::class)
class ApiTest {
    @Rule @JvmField val folder = TemporaryFolder()
    lateinit var main: Main

    @Before fun setup() {
        main = Main(folder.root.apply { mkdirs() })
    }

    fun testApp(func: TestApplicationEngine.() -> Unit) {
        withTestApplication {
            with(main) { application.setup() }
            func()
        }
    }

    @ContextDsl
    fun <T : Any, U: Any> TestApplicationEngine.post(
        path: String,
        requestSerializer: KSerializer<T>,
        request: T,
        responseSerializer: KSerializer<U>): U =
        handleRequest(HttpMethod.Post, path) {
            addHeader("Content-Type", "application/json")
            setBody(Json.stringify(requestSerializer, request))
        }.run {
            assertEquals(HttpStatusCode.OK, response.status())
            Json.parse(responseSerializer, response.content!!)
        }

    @ContextDsl
    fun <U: Any> TestApplicationEngine.get(
        path: String,
        responseSerializer: KSerializer<U>): U =
        handleRequest(HttpMethod.Get, path).run {
            assertEquals(HttpStatusCode.OK, response.status())
            println(response.content)
            Json.parse(responseSerializer, response.content!!)
        }

    @Test fun `get home`() {
        testApp {
            assertEquals(listOf(), get("", Home.serializer()).someBirds)
        }
    }

    @Test fun `post and get bird`() {
        testApp {
            val birdsPath = get("", Home.serializer()).birds
            val bird = post(birdsPath, CreateBird.serializer(), CreateBird("ostrich"), Bird.serializer())
            assertEquals("ostrich", bird.name)
            assertEquals(bird, get(bird.self, Bird.serializer()))
        }
    }

    @Test fun `create and get chirp`() {
        testApp {
            val birdsPath = get("", Home.serializer()).birds
            val bird = post(birdsPath, CreateBird.serializer(), CreateBird("ostrich"), Bird.serializer())
            val chirp = post(bird.chirps, CreateChirp.serializer(), CreateChirp("hello"), Chirp.serializer())
            assertEquals("hello", chirp.text)
            assertEquals(chirp, get(chirp.self, Chirp.serializer()))
        }
    }

    @Test fun `react to chirp`() {
        testApp {
            val birdsPath = get("", Home.serializer()).birds
            val ostrich = post(birdsPath, CreateBird.serializer(), CreateBird("ostrich"), Bird.serializer())
            val chirp = post(ostrich.chirps, CreateChirp.serializer(), CreateChirp("hello"), Chirp.serializer())
            val roadrunner = post(birdsPath, CreateBird.serializer(), CreateBird("roadrunner"), Bird.serializer())
            val newChirp = post(chirp.react, ChirpReact.serializer(), ChirpReact(roadrunner.self, "+"), Chirp.serializer())
            assertEquals("+", newChirp.reactions[roadrunner.self])
        }
    }
}
