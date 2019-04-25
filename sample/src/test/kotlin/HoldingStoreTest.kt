import io.gladed.watchable.Watcher
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import model.Bird
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import store.Cannot
import store.Hold
import store.HoldingStore
import store.Store

@UseExperimental(ObsoleteCoroutinesApi::class)
class HoldingStoreTest {
    private val robin = Bird(name = "robin")

    private val rootStore = mockk<Store<Bird>>(relaxUnitFun = true)

    interface WatchCreator {
        suspend fun create(bird: Bird): Hold
    }

    private val creator = mockk<WatchCreator>()

    private val hold = mockk<Hold>(relaxUnitFun = true)

    interface ScopedStoreTest : TestCoroutineScope {
        val scopeStore: HoldingStore<Bird>
    }

    private fun test(func: suspend ScopedStoreTest.() -> Unit) = runTest {
        (object : ScopedStoreTest, TestCoroutineScope by this {
            override val scopeStore = HoldingStore(coroutineContext, rootStore) { creator.create(this) }
        }).func()
    }

    @Before
    fun setup() {
        coEvery { creator.create(robin) } returns hold
        coEvery { rootStore.get(robin.id) } returns robin
    }

    @Test fun `put an item`() = test {
        inScope {
            scopeStore.create(this).put(robin.id, robin)
            coVerify { rootStore.put(robin.id, robin) }
            coVerify { creator.create(robin) }
        }
    }

    @Test fun `release when scope completes`() = test {
        inScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
        }

        coVerify { creator.create(robin) }
        coVerify { hold.stop() }
    }

    @Test fun `hold only once when two scopes get`() = test {
        inScope {
            assertEquals(robin, scopeStore.create(this).get(robin.id))
            coVerify(exactly = 1) { creator.create(robin) }
            inScope {
                assertEquals(robin, scopeStore.create(this).get(robin.id))
                coVerify(exactly = 1) { creator.create(robin) }
            }
        }
    }

    @Test fun `release only when both scopes close`() = test {
        inScope {
            scopeStore.create(this).get(robin.id)
            inScope {
                scopeStore.create(this).get(robin.id)
            }
            coVerify(exactly = 0) { hold.stop() }
        }
        coVerify(exactly = 1) { hold.stop() }
    }

    @Test fun `hold+release only once when get twice from same scope`() = test {
        inScope {
            val store = scopeStore.create(this)
            store.get(robin.id)
            store.get(robin.id)
            coVerify(exactly = 1) { creator.create(robin) }
        }
        coVerify(exactly = 1) { hold.stop() }
    }

    @Test fun `second user must reallocate`() = test {
        inScope { scopeStore.create(this).get(robin.id) }
        inScope { scopeStore.create(this).get(robin.id) }

        coVerify(exactly = 2) { creator.create(robin) }
        coVerify(exactly = 2) { hold.stop() }
    }

    @Test fun `failure to get causes throw`() = test {
        coEvery { rootStore.get(eq(robin.id)) } throws Cannot("find value for that key")

        inScope {
            val store = scopeStore.create(this)
            try {
                store.get(robin.id)
                fail("Should have failed")
            } catch (c: Cannot) {
                println("Threw $c (as expected)")
            }
        }
        testContext.assertAllUnhandledExceptions { true }
    }

    @Test fun `second attempt succeeds`() = test {
        coEvery { rootStore.get(robin.id) } throws Cannot("find value for that key")

        inScope {
            val store = scopeStore.create(this)
            try {
                store.get(robin.id)
            } catch (c: Exception) { }

            coEvery { rootStore.get(robin.id) } returns robin
            assertEquals(robin, store.get(robin.id))
        }
        testContext.assertAllUnhandledExceptions { true }
    }

    @Test fun `two attempts collude`() = test {
        val scope1 = newScope()
        val scope2 = newScope()

        val birds = listOf(async {
            scopeStore.create(scope1).get(robin.id)
        }, async {
            scopeStore.create(scope2).get(robin.id)
        }).awaitAll()
        assertEquals(listOf(robin, robin), birds)

        // Should only be one hold and one release even though two scopes were getting
        coVerify(exactly = 1) { creator.create(robin) }

        scope1.cancel()
        trigger()
        coVerify(exactly = 0) { hold.stop() }
        scope2.cancel()
        trigger()
        coVerify(exactly = 1) { hold.stop() }
    }

    @Test fun `attempt to get is cancelled when scope dies`() = test {
        val mutex = Mutex(locked = true)
        coEvery { rootStore.get(robin.id) } coAnswers {
            mutex.withLock { robin }
        }
        inScope {
            val store = scopeStore.create(this)
            val deferred = async {
                store.get(robin.id)
            }
            deferred.cancel()

            // Return to normal function
            coEvery { rootStore.get(eq(robin.id)) } returns robin
            assertEquals(robin, store.get(robin.id))
        }
    }

    @Test fun `stop everybody`() = test {
        inScope {
            scopeStore.create(this).get(robin.id)
            scopeStore.stop()
            // Released even though scope still active
            coVerify { hold.stop() }
        }
    }

    // TODO: Throw during watch itself
    // TODO: Throw during start() of watch
}
