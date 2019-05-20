import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import kotlin.test.assertEquals

class ApiTest {
    val main = Main()

    // TODO: Configure a temporary data location

    fun testApp(func: TestApplicationEngine.() -> Unit) {
        withTestApplication {
            with(main) { application.setup() }
        }
    }
    @Test fun `get home`() {
        testApp {
            with(handleRequest(HttpMethod.Get, "")) {
                assertEquals(HttpStatusCode.OK, response.status())
                println(response.content)
            }
        }
    }
}
