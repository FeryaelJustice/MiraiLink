package com.feryaeljustice.mirailink.ui.screens.home

import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.model.swipe.UndoSwipeResult
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.GetUndoQuotaUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.UndoSwipeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase by inject()
    private val sendLocationPingUseCase: SendLocationPingUseCase by inject()
    private val getUndoQuotaUseCase: GetUndoQuotaUseCase by inject()
    private val undoSwipeUseCase: UndoSwipeUseCase by inject()

    private lateinit var viewModel: HomeViewModel
    private val preferences = MutableStateFlow(SearchPreferences())

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<GetFeedUseCase>() }
                    single { mockk<LikeUserUseCase>() }
                    single { mockk<DislikeUserUseCase>() }
                    single { mockk<GetCurrentUserUseCase>() }
                    single { mockk<GetSearchPreferencesUseCase>() }
                    single { mockk<SendLocationPingUseCase>(relaxed = true) }
                    single { mockk<GetUndoQuotaUseCase>() }
                    single { mockk<UndoSwipeUseCase>() }
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
        every { getSearchPreferencesUseCase.invoke() } returns preferences
        coEvery { getFeedUseCase.invoke() } returns
            MiraiLinkResult.Success(
                listOf(
                    user1,
                    user2,
                ),
            )
        coEvery { getUndoQuotaUseCase.invoke() } returns
            MiraiLinkResult.Success(
                UndoQuota(
                    tier = SubscriptionPlanType.FREE,
                    maxUndos = 1,
                    usedUndos = 0,
                    remainingUndos = 1,
                    resetsAt = null,
                    hasUndoableSwipe = false,
                    canUndo = true,
                ),
            )

        viewModel =
            HomeViewModel(
                getFeedUseCase,
                likeUserUseCase,
                dislikeUserUseCase,
                getCurrentUserUseCase,
                getSearchPreferencesUseCase,
                sendLocationPingUseCase,
                getUndoQuotaUseCase,
                undoSwipeUseCase,
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
    fun `saved search preference change automatically reloads feed`() =
        runTest {
            preferences.value = SearchPreferences(radiusKm = 80f)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coVerify(exactly = 2) { getFeedUseCase.invoke() }
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
            coEvery { undoSwipeUseCase.invoke("1") } returns
                MiraiLinkResult.Success(
                    UndoSwipeResult(
                        user = user1,
                        actionUndone = "dislike",
                        quota = UndoQuota(
                            tier = SubscriptionPlanType.FREE,
                            maxUndos = 1,
                            usedUndos = 1,
                            remainingUndos = 0,
                            resetsAt = null,
                            hasUndoableSwipe = false,
                            canUndo = false,
                        ),
                    ),
                )

            viewModel.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val canUndo = viewModel.canUndo()
            assert(canUndo)

            val undone = viewModel.undoSwipe()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(undone)
            coVerify { undoSwipeUseCase.invoke("1") }
            val state = viewModel.state.value
            assert(state is HomeViewModel.HomeUiState.Success)
            assert((state as HomeViewModel.HomeUiState.Success).visibleUsers.first().id == "1")
        }

    @Test
    fun `undo swipe when quota exhausted emits NavigateToPaywall`() =
        runTest {
            coEvery { dislikeUserUseCase.invoke("1") } returns MiraiLinkResult.Success(Unit)
            coEvery { getUndoQuotaUseCase.invoke() } returns
                MiraiLinkResult.Success(
                    UndoQuota(
                        tier = SubscriptionPlanType.FREE,
                        maxUndos = 1,
                        usedUndos = 1,
                        remainingUndos = 0,
                        resetsAt = null,
                        hasUndoableSwipe = true,
                        canUndo = false,
                    ),
                )

            val zeroQuotaVm =
                HomeViewModel(
                    getFeedUseCase,
                    likeUserUseCase,
                    dislikeUserUseCase,
                    getCurrentUserUseCase,
                    getSearchPreferencesUseCase,
                    sendLocationPingUseCase,
                    getUndoQuotaUseCase,
                    undoSwipeUseCase,
                    mainCoroutineRule.testDispatcher,
                )
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            zeroQuotaVm.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            var paywallEmitted = false
            val job =
                launch(start = kotlinx.coroutines.CoroutineStart.UNDISPATCHED) {
                    zeroQuotaVm.events.collect { event ->
                        if (event is HomeViewModel.HomeEvent.NavigateToPaywall) {
                            paywallEmitted = true
                        }
                    }
                }

            val result = zeroQuotaVm.undoSwipe()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(!result)
            assert(paywallEmitted)
            job.cancel()
        }

    @Test
    fun `undo swipe error DAILY_UNDO_LIMIT_REACHED emits NavigateToPaywall`() =
        runTest {
            coEvery { dislikeUserUseCase.invoke("1") } returns MiraiLinkResult.Success(Unit)
            coEvery { undoSwipeUseCase.invoke("1") } returns
                MiraiLinkResult.Error(SubscriptionError.DAILY_UNDO_LIMIT_REACHED)

            viewModel.swipeLeft()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            var paywallEmitted = false
            val job =
                launch(start = kotlinx.coroutines.CoroutineStart.UNDISPATCHED) {
                    viewModel.events.collect { event ->
                        if (event is HomeViewModel.HomeEvent.NavigateToPaywall) {
                            paywallEmitted = true
                        }
                    }
                }

            viewModel.undoSwipe()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(paywallEmitted)
            job.cancel()
        }
}
