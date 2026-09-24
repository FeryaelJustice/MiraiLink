package com.feryaeljustice.mirailink.ui.screens.explore.feed

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.UpdateCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryFeedViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var getCategoryFeedUseCase: GetCategoryFeedUseCase
    private lateinit var getCategoryPreferencesUseCase: GetCategoryPreferencesUseCase
    private lateinit var updateCategoryPreferencesUseCase: UpdateCategoryPreferencesUseCase
    private lateinit var likeUserUseCase: LikeUserUseCase
    private lateinit var dislikeUserUseCase: DislikeUserUseCase
    private lateinit var viewModel: CategoryFeedViewModel

    private val sampleUser1 =
        User(
            id = "user-1",
            username = "sasuke",
            nickname = "Sasuke",
            email = "sasuke@leaf.com",
            phoneNumber = null,
            bio = "Avenger",
            gender = "male",
            birthdate = "2000-01-01",
            games = emptyList(),
            animes = emptyList(),
        )

    private val sampleUser2 =
        User(
            id = "user-2",
            username = "sakura",
            nickname = "Sakura",
            email = "sakura@leaf.com",
            phoneNumber = null,
            bio = "Medic",
            gender = "female",
            birthdate = "2000-01-01",
            games = emptyList(),
            animes = emptyList(),
        )

    private val samplePreference =
        CategoryPreference(
            categoryId = "cat-1",
            radiusKm = 75,
        )

    @Before
    fun setUp() {
        getCategoryFeedUseCase = mockk()
        getCategoryPreferencesUseCase = mockk()
        updateCategoryPreferencesUseCase = mockk()
        likeUserUseCase = mockk()
        dislikeUserUseCase = mockk()

        coEvery { getCategoryPreferencesUseCase("cat-1") } returns MiraiLinkResult.Success(samplePreference)
        coEvery { getCategoryFeedUseCase("cat-1") } returns MiraiLinkResult.Success(listOf(sampleUser1, sampleUser2))
    }

    private fun createViewModel(): CategoryFeedViewModel =
        CategoryFeedViewModel(
            categoryId = "cat-1",
            categoryName = "Anime Lovers",
            getCategoryFeedUseCase = getCategoryFeedUseCase,
            getCategoryPreferencesUseCase = getCategoryPreferencesUseCase,
            updateCategoryPreferencesUseCase = updateCategoryPreferencesUseCase,
            likeUserUseCase = likeUserUseCase,
            dislikeUserUseCase = dislikeUserUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
        )

    @Test
    fun init_loadsPreferencesAndFeedSuccessfully() = runTest {
        viewModel = createViewModel()
        advanceUntilIdle()

        assertEquals(75, viewModel.radiusKm.value)
        val state = viewModel.state.value
        assertTrue(state is CategoryFeedViewModel.CategoryFeedUiState.Success)
        val success = state as CategoryFeedViewModel.CategoryFeedUiState.Success
        assertEquals(2, success.visibleUsers.size)
        assertEquals("user-1", success.visibleUsers[0].id)
    }

    @Test
    fun loadFeed_emitsEmpty_whenNoUsersReturned() = runTest {
        coEvery { getCategoryFeedUseCase("cat-1") } returns MiraiLinkResult.Success(emptyList())

        viewModel = createViewModel()
        advanceUntilIdle()

        assertTrue(viewModel.state.value is CategoryFeedViewModel.CategoryFeedUiState.Empty)
    }

    @Test
    fun loadFeed_emitsError_whenFeedFails() = runTest {
        coEvery { getCategoryFeedUseCase("cat-1") } returns MiraiLinkResult.Error(UnknownError)

        viewModel = createViewModel()
        advanceUntilIdle()

        assertTrue(viewModel.state.value is CategoryFeedViewModel.CategoryFeedUiState.Error)
    }

    @Test
    fun updateRadius_updatesRadiusKmAndReloadsFeed() = runTest {
        coEvery { updateCategoryPreferencesUseCase("cat-1", 150) } returns MiraiLinkResult.Success(
            CategoryPreference("cat-1", 150),
        )

        viewModel = createViewModel()
        advanceUntilIdle()

        var callbackCalled = false
        viewModel.updateRadius(150) { callbackCalled = true }
        advanceUntilIdle()

        assertTrue(callbackCalled)
        assertEquals(150, viewModel.radiusKm.value)
        assertFalse(viewModel.isSavingPreferences.value)
        assertFalse(viewModel.showSettingsSheet.value)
        coVerify(exactly = 1) { updateCategoryPreferencesUseCase("cat-1", 150) }
    }

    @Test
    fun swipeRight_callsLikeAndAdvancesQueue() = runTest {
        coEvery { likeUserUseCase("user-1") } returns MiraiLinkResult.Success(true)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.swipeRight()
        advanceUntilIdle()

        coVerify(exactly = 1) { likeUserUseCase("user-1") }
        val state = viewModel.state.value
        assertTrue(state is CategoryFeedViewModel.CategoryFeedUiState.Success)
        val success = state as CategoryFeedViewModel.CategoryFeedUiState.Success
        assertEquals(1, success.visibleUsers.size)
        assertEquals("user-2", success.visibleUsers[0].id)
    }

    @Test
    fun swipeLeft_callsDislikeAndAdvancesQueue() = runTest {
        coEvery { dislikeUserUseCase("user-1") } returns MiraiLinkResult.Success(Unit)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.swipeLeft()
        advanceUntilIdle()

        coVerify(exactly = 1) { dislikeUserUseCase("user-1") }
        val state = viewModel.state.value
        assertTrue(state is CategoryFeedViewModel.CategoryFeedUiState.Success)
        val success = state as CategoryFeedViewModel.CategoryFeedUiState.Success
        assertEquals(1, success.visibleUsers.size)
        assertEquals("user-2", success.visibleUsers[0].id)
    }

    @Test
    fun undoSwipe_restoresUserToQueue() = runTest {
        coEvery { likeUserUseCase("user-1") } returns MiraiLinkResult.Success(true)

        viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.swipeRight()
        advanceUntilIdle()

        val restored = viewModel.undoSwipe()
        assertTrue(restored)

        val state = viewModel.state.value
        assertTrue(state is CategoryFeedViewModel.CategoryFeedUiState.Success)
        val success = state as CategoryFeedViewModel.CategoryFeedUiState.Success
        assertEquals(2, success.visibleUsers.size)
        assertEquals("user-1", success.visibleUsers[0].id)
    }

    @Test
    fun openAndCloseSettingsSheet_updatesState() {
        viewModel = createViewModel()
        assertFalse(viewModel.showSettingsSheet.value)

        viewModel.openSettingsSheet()
        assertTrue(viewModel.showSettingsSheet.value)

        viewModel.closeSettingsSheet()
        assertFalse(viewModel.showSettingsSheet.value)
    }
}
