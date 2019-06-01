import kotlin.browser.*

actual class Sample {
    actual fun checkMe() = 12
}

actual object Platform {
    actual val name: String = "JS"
}

@Suppress("unused")
@JsName("helloWorld")
fun helloWorld(salutation: String) {
    val message = "${hello()} ($salutation from JS)"
    document.getElementById("js-response")?.textContent = message
}