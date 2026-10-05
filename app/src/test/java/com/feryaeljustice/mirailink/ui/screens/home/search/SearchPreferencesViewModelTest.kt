package com.feryaeljustice.mirailink.ui.screens.home.search

import com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.SaveSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPreferencesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase = mockk()
    private val saveSearchPreferencesUseCase: SaveSearchPreferencesUseCase = mockk()
    private val sendLocationPingUseCase: SendLocationPingUseCase = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()

    private lateinit var viewModel: SearchPreferencesViewModel

    @Before
    fun setUp() {
        coEvery { getSearchPreferencesUseCase() } returns flowOf(
            SearchPreferences(
                radiusKm = 50f,
                scope = SearchScope.RADIUS_RESIDENCE,
                searchGender = TargetSearchGender.ALL,
            ),
        )
        coEvery { getCurrentUserUseCase() } returns MiraiLinkResult.Success(
            com.feryaeljustice.mirailink.domain.model.user.User(
                id = "1",
                username = "testuser",
                nickname = "testuser",
                email = "test@example.com",
                phoneNumber = null,
                bio = null,
                gender = "male",
                birthdate = null,
                photos = emptyList(),
                games = emptyList(),
                animes = emptyList(),
            ),
        )

        viewModel = SearchPreferencesViewModel(
            getSearchPreferencesUseCase = getSearchPreferencesUseCase,
            saveSearchPreferencesUseCase = saveSearchPreferencesUseCase,
            sendLocationPingUseCase = sendLocationPingUseCase,
            getCurrentUserUseCase = getCurrentUserUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
            mainDispatcher = mainCoroutineRule.testDispatcher,
        )
    }

    @Test
    fun `save normalizes searchGender to ALL when isPlusOrPremium is false`() = runTest {
        viewModel.updateDraftSearchGender(TargetSearchGender.FEMALE)
        coEvery { saveSearchPreferencesUseCase(any()) } returns MiraiLinkResult.Success(Unit)

        var successCalled = false
        viewModel.save(isPlusOrPremium = false) {
            successCalled = true
        }
        advanceUntilIdle()

        assertEquals(true, successCalled)
        assertEquals(TargetSearchGender.ALL, viewModel.draftSearchGender.value)
        coVerify(exactly = 1) {
            saveSearchPreferencesUseCase(match { it.searchGender == TargetSearchGender.ALL })
        }
    }

    @Test
    fun `save preserves selected gender when isPlusOrPremium is true`() = runTest {
        viewModel.updateDraftSearchGender(TargetSearchGender.FEMALE)
        coEvery { saveSearchPreferencesUseCase(any()) } returns MiraiLinkResult.Success(Unit)

        var successCalled = false
        viewModel.save(isPlusOrPremium = true) {
            successCalled = true
        }
        advanceUntilIdle()

        assertEquals(true, successCalled)
        assertEquals(TargetSearchGender.FEMALE, viewModel.draftSearchGender.value)
        coVerify(exactly = 1) {
            saveSearchPreferencesUseCase(match { it.searchGender == TargetSearchGender.FEMALE })
        }
    }
}
