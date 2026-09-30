package com.feryaeljustice.mirailink.ui.screens.likes

import com.feryaeljustice.mirailink.domain.model.swipe.ReceivedLike
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.swipe.GetReceivedLikesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReceivedLikesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getReceivedLikesUseCase: GetReceivedLikesUseCase = mockk()
    private val likeUserUseCase: LikeUserUseCase = mockk()
    private val session: GlobalMiraiLinkSession = mockk(relaxed = true)

    private val isPremiumFlow = MutableStateFlow(false)

    @Before
    fun setUp() {
        every { session.isPremium } returns isPremiumFlow
    }

    @Test
    fun `when user is not premium, uiState is locked`() = runTest {
        isPremiumFlow.value = false
        val viewModel = ReceivedLikesViewModel(
            getReceivedLikesUseCase = getReceivedLikesUseCase,
            likeUserUseCase = likeUserUseCase,
            globalMiraiLinkSession = session,
        )
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.isPremiumLocked)
        assertTrue(viewModel.uiState.value.likes.isEmpty())
    }

    @Test
    fun `when user becomes premium, uiState unlocks and loads likes`() = runTest {
        isPremiumFlow.value = false
        val mockUser: User = mockk(relaxed = true) {
            every { id } returns "u1"
            every { username } returns "alice"
            every { nickname } returns "Alice"
            every { birthdate } returns "2000-01-01"
            every { photos } returns emptyList()
        }
        val sampleLike = ReceivedLike(
            likeId = "like1",
            likedAt = "2026-09-30T10:00:00Z",
            user = mockUser,
        )
        coEvery { getReceivedLikesUseCase() } returns MiraiLinkResult.Success(listOf(sampleLike))

        val viewModel = ReceivedLikesViewModel(
            getReceivedLikesUseCase = getReceivedLikesUseCase,
            likeUserUseCase = likeUserUseCase,
            globalMiraiLinkSession = session,
        )
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isPremiumLocked)

        // Switch to premium
        isPremiumFlow.value = true
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isPremiumLocked)
        assertEquals(1, viewModel.uiState.value.likes.size)
        assertEquals("alice", viewModel.uiState.value.likes[0].username)
    }
}
