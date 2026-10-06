package com.feryaeljustice.mirailink.ui.screens.chat

import com.feryaeljustice.mirailink.domain.model.capsule.*
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.*
import com.feryaeljustice.mirailink.domain.usecase.capsule.CapsuleUseCases
import com.feryaeljustice.mirailink.domain.usecase.users.*
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CrystalChatViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var vm: ChatViewModel
    private val repository = mockk<CapsuleRepository>()
    private val snapshot = CrystalCapsule("capsule", listOf("own", "peer"), progress = 2, level = 1, revision = 5)
    private var server = snapshot
    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        coEvery { repository.cached("peer") } returns snapshot
        coEvery { repository.history("peer") } coAnswers { MiraiLinkResult.Success(CapsuleConversation(emptyList(), server)) }
        vm = ChatViewModel(mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true),
            mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true), mockk(relaxed = true),
            dispatcher, CapsuleUseCases(repository))
    }
    @After fun close() { vm.stopMessagePolling(); Dispatchers.resetMain() }

    @Test fun `history and resuming never replay haptics but a new foreground level emits once`() = runTest(dispatcher) {
        val effects = mutableListOf<Int>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.unlockEffects.collect { effects += it } }
        vm.setScreenActive(true)
        vm.getMessages("peer"); runCurrent()
        assertTrue(effects.isEmpty())
        server = server.copy(level = 2, progress = 4, revision = 7)
        vm.getMessages("peer"); runCurrent()
        vm.getMessages("peer"); runCurrent()
        assertEquals(listOf(2), effects)
        vm.setScreenActive(false)
        server = server.copy(level = 3, progress = 6, revision = 9)
        vm.getMessages("peer"); runCurrent()
        vm.setScreenActive(true)
        vm.getMessages("peer"); runCurrent()
        assertEquals(listOf(2), effects)
    }
    @Test fun `slow polling requests stay sequential even with a manual refresh`() = runTest(dispatcher) {
        var inFlight = 0
        var maximum = 0
        coEvery { repository.history("peer") } coAnswers {
            inFlight++; maximum = maxOf(maximum, inFlight)
            delay(5000)
            inFlight--
            MiraiLinkResult.Success(CapsuleConversation(emptyList(), snapshot))
        }
        vm.startMessagePolling("peer"); runCurrent()
        vm.getMessages("peer"); runCurrent()
        advanceTimeBy(16000); runCurrent()
        vm.stopMessagePolling()
        advanceUntilIdle()
        assertEquals(1, maximum)
    }
    @Test fun `failed capsule action retry reuses its original identifier and never awards optimistic progress`() = runTest(dispatcher) {
        vm.getMessages("peer"); runCurrent()
        val ids = mutableListOf<String>()
        coEvery { repository.act(any(), any()) } coAnswers {
            ids += secondArg<CapsuleAction>().actionId
            MiraiLinkResult.Error(com.feryaeljustice.mirailink.domain.error.DataError.Network.TIMEOUT)
        }
        vm.capsuleAction("pause"); runCurrent()
        assertEquals(snapshot, vm.capsule.value)
        assertTrue(vm.pendingWork.value)
        vm.retryPending(); runCurrent()
        assertEquals(2, ids.size); assertEquals(ids[0], ids[1])
        assertEquals(snapshot, vm.capsule.value)
    }
}
