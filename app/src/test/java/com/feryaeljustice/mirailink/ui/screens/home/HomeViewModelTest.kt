package com.feryaeljustice.mirailink.ui.screens.home

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class HomeViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getFeedUseCase: GetFeedUseCase by inject()
    private val likeUserUseCase: LikeUserUseCase by inject()
    private val dislikeUserUseCase: DislikeUserUseCase by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()

    private lateinit var viewModel: HomeViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<GetFeedUseCase>() }
                    single { mockk<LikeUserUseCase>() }
                    single { mockk<DislikeUserUseCase>() }
                    single { mockk<GetCurrentUserUseCase>() }
                },
            )
        }

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
        coEvery { getCurrentUserUseCase.invoke() } returns MiraiLinkResult.Success(user1)
        coEvery { getFeedUseCase.invoke() } returns
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
            coEvery { likeUserUseCase.invoke("1") } returns MiraiLinkResult.Success(true)

            viewModel.swipeRight()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coVerify { likeUserUseCase.invoke("1") }
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "2")
        }

    @Test
    fun `swipe left calls dislike use case`() =
        runTest {
            coEvery { dislikeUserUseCase.invoke("1") } returns MiraiLinkResult.Success(Unit)

            viewModel.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coVerify { dislikeUserUseCase.invoke("1") }
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "2")
        }

    @Test
    fun `undo swipe restores user`() =
        runTest {
            coEvery { dislikeUserUseCase.invoke("1") } returns MiraiLinkResult.Success(Unit)
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
