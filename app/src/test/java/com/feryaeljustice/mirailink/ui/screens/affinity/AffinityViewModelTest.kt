package com.feryaeljustice.mirailink.ui.screens.affinity

import androidx.lifecycle.SavedStateHandle
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.affinity.*
import com.feryaeljustice.mirailink.domain.usecase.affinity.AffinityUseCases
import com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class AffinityViewModelTest {
    @get:Rule val main = MainCoroutineRule()
    private val cases = mockk<AffinityUseCases>()
    private val reports = mockk<ReportUseCase>()
    private val session = mockk<GlobalMiraiLinkSession>()
    private val request = AffinityRequest("request", true, "pending", "Hello", Instant.now().plusSeconds(3600).toString(), AffinityPerson("peer", "test", "Test"))

    @Before fun setup() {
        every { session.isPlus } returns MutableStateFlow(true)
        every { session.isDemoMode } returns MutableStateFlow(false)
        every { session.currentUserId } returns MutableStateFlow("owner")
        coEvery { cases.feed() } returns MiraiLinkResult.Success(AffinityFeed(enabled = true))
        coEvery { cases.likes(any()) } returns MiraiLinkResult.Success(AffinityLikes())
        coEvery { cases.requests(any()) } returns MiraiLinkResult.Success(AffinityRequests(listOf(request)))
    }

    @Test fun `resolved and expired invitations stay out of the pending inbox`() = runTest {
        coEvery { cases.requests(any()) } returns MiraiLinkResult.Success(AffinityRequests(listOf(
            request, request.copy(id = "accepted", state = "accepted"), request.copy(id = "rejected", state = "rejected"),
            request.copy(id = "expired", expiresAt = Instant.now().minusSeconds(1).toString()),
        )))
        val vm = AffinityViewModel(cases, session, SavedStateHandle(), reports)
        runCurrent()
        assertEquals(listOf("request"), vm.state.value.requests.map { it.id })
    }

    @Test fun `acceptance removes the card even if the subsequent refresh fails`() = runTest {
        val vm = AffinityViewModel(cases, session, SavedStateHandle(), reports)
        runCurrent()
        coEvery { cases.respond("request", true) } returns MiraiLinkResult.Success(AffinityAction("request", "accepted", "chat"))
        coEvery { cases.requests(any()) } returns MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE)
        vm.respond("request", true)
        runCurrent()
        assertTrue(vm.state.value.requests.isEmpty())
        assertNotNull(vm.state.value.error)
    }

    @Test fun `rejection removes the card and a failed rejection preserves it`() = runTest {
        val vm = AffinityViewModel(cases, session, SavedStateHandle(), reports)
        runCurrent()
        coEvery { cases.respond("request", false) } returns MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE)
        vm.respond("request", false)
        runCurrent()
        assertEquals(1, vm.state.value.requests.size)
        coEvery { cases.respond("request", false) } returns MiraiLinkResult.Success(AffinityAction("request", "rejected"))
        coEvery { cases.requests(any()) } returns MiraiLinkResult.Success(AffinityRequests(listOf(request.copy(state = "rejected"))))
        vm.respond("request", false)
        runCurrent()
        assertTrue(vm.state.value.requests.isEmpty())
    }

    @Test fun `reported invitations do not reappear from unchanged Demo data`() = runTest {
        val vm = AffinityViewModel(cases, session, SavedStateHandle(), reports)
        runCurrent()
        coEvery { reports("peer", "spam") } returns MiraiLinkResult.Success(Unit)
        vm.openReport("peer")
        vm.editReport("spam")
        vm.report()
        runCurrent()
        assertTrue(vm.state.value.requests.isEmpty())
        assertNull(vm.state.value.reportPeer)
        vm.refresh()
        runCurrent()
        assertTrue(vm.state.value.requests.isEmpty())
    }
}
