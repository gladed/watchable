import external.Adapter
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import kotlinx.serialization.KSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import rest.Bird
import rest.Chirp
import rest.CreateBird
import rest.CreateChirp
import rest.Home
import kotlin.test.assertEquals

@OptIn(UnstableDefault::class)
class ApiTest {
    @Rule @JvmField val folder = TemporaryFolder()

    private fun testApp(func: TestApplicationEngine.() -> Unit) {
        withTestApplication {
            val dataDir = folder.root.apply { mkdirs() }
            val logic = Adapter.createLogic(coroutineContext, dataDir)
            application.bind(logic)
            func()
        }
    }

    fun <U: Any> TestApplicationEngine.get(
        path: String,
        responseSerializer: KSerializer<U>): U =
        handleRequest(HttpMethod.Get, "/api$path").run {
            assertEquals(HttpStatusCode.OK, response.status())
            println(response.content)
            Json.parse(responseSerializer, response.content!!)
        }

    fun <T : Any, U: Any> TestApplicationEngine.post(
        path: String,
        requestSerializer: KSerializer<T>,
        request: T,
        responseSerializer: KSerializer<U>): U =
        handleRequest(HttpMethod.Post, "/api$path") {
            addHeader("Content-Type", "application/json")
            setBody(Json.stringify(requestSerializer, request))
        }.run {
            assertEquals(HttpStatusCode.OK, response.status())
            Json.parse(responseSerializer, response.content!!)
        }

    @Test fun `get home`() {
        testApp {
            assertEquals(listOf(), get("", Home.serializer()).birds)
        }
    }

    @Test fun `post and get bird`() {
        testApp {
            val birdsPath = get("", Home.serializer()).bird
            val bird = post(birdsPath, CreateBird.serializer(), CreateBird("ostrich"), Bird.serializer())
            assertEquals("ostrich", bird.name)
            assertEquals(bird, get(bird.self, Bird.serializer()))
        }
    }

    @Test fun `create and get chirp`() {
        testApp {
            val birdsPath = get("", Home.serializer()).bird
            val bird = post(birdsPath, CreateBird.serializer(), CreateBird("ostrich"), Bird.serializer())
            val chirp = post(bird.chirps, CreateChirp.serializer(), CreateChirp("hello"), Chirp.serializer())
            assertEquals("hello", chirp.text)
            assertEquals(chirp, get(chirp.self, Chirp.serializer()))
        }
    }
}
