import junit.framework.TestFailure
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Executors

class ChangeWatcherRule<C> : TestRule, CoroutineScope {

    private val dispatcher = Executors.newSingleThreadExecutor {
        task -> Thread(task, "change-watcher")
    }.asCoroutineDispatcher()

    override val coroutineContext = dispatcher + Job() + CoroutineExceptionHandler { _, cause -> log(cause.toString())}

    private val changes = Channel<C>(Channel.UNLIMITED)

    operator fun plusAssign(change: C) {
        log("Change: $change")
        launch {
            changes.send(change)
        }
    }

    operator fun plusAssign(changeList: List<C>) {
        log("Changes: $changeList")
        launch {
            changeList.forEach { changes.send(it) }
        }
    }

    suspend fun expect(vararg expected: C, timeout: Long = 250) {
        val remaining = expected.toMutableList()
        val result = withTimeoutOrNull(timeout) {
            while (remaining.isNotEmpty()) {
                assertEquals(remaining.first(), changes.receive())
                remaining.removeAt(0)
            }
        }
        if (result == null) {
            fail("Never received ${remaining.first()} in ${timeout}ms")
        }
    }

    suspend fun expectNone() {
        delay(25)
        assertEquals(null, changes.poll())
    }

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            try {
                base.evaluate()
            } finally {
                coroutineContext[Job]!!.cancel()
            }
        }
    }
}
