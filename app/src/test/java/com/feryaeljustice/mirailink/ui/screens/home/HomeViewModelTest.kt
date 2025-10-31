// Author: Feryael Justice
// Date: 2024-07-29

package com.feryaeljustice.mirailink.ui.screens.home

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private val getFeedUseCase: Lazy<GetFeedUseCase> = mockk()
    private val likeUserUseCase: Lazy<LikeUserUseCase> = mockk()
    private val dislikeUserUseCase: Lazy<DislikeUserUseCase> = mockk()
    private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase> = mockk()

    private val user1 =
        User(
            "1",
            "user1",
            "user1",
            "user1@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    private val user2 =
        User(
            "2",
            "user2",
            "user2",
            "user2@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )

    @Before
    fun setUp() {
        coEvery { getCurrentUserUseCase.get().invoke() } returns MiraiLinkResult.Success(user1)
        coEvery { getFeedUseCase.get().invoke() } returns
            MiraiLinkResult.Success(
                listOf(
                    user1,
                    user2,
                ),
            )

        viewModel =
            HomeViewModel(
                getFeedUseCase,
                likeUserUseCase,
                dislikeUserUseCase,
                getCurrentUserUseCase,
                mainCoroutineRule.testDispatcher,
            )
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `load users success`() =
        runTest {
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.size == 2)
        }

    @Test
    fun `swipe right calls like use case`() =
        runTest {
            coEvery { likeUserUseCase.get().invoke("1") } returns MiraiLinkResult.Success(true)

            viewModel.swipeRight()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coVerify { likeUserUseCase.get().invoke("1") }
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "2")
        }

    @Test
    fun `swipe left calls dislike use case`() =
        runTest {
            coEvery { dislikeUserUseCase.get().invoke("1") } returns MiraiLinkResult.Success(Unit)

            viewModel.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coVerify { dislikeUserUseCase.get().invoke("1") }
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "2")
        }

    @Test
    fun `undo swipe restores user`() =
        runTest {
            coEvery { dislikeUserUseCase.get().invoke("1") } returns MiraiLinkResult.Success(Unit)
            viewModel.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            // Allow undo
            viewModel.lastUndoTime = 0
            val canUndo = viewModel.canUndo()
            assert(canUndo)

            val undone = viewModel.undoSwipe()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(undone)
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "1")
        }
}
