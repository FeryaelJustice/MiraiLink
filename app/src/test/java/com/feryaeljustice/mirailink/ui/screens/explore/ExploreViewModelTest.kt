package com.feryaeljustice.mirailink.ui.screens.explore

import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSection
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.usecase.explore.GetExploreSectionsUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var getExploreSectionsUseCase: GetExploreSectionsUseCase
    private lateinit var viewModel: ExploreViewModel

    private val sampleCategory =
        ExploreCategory(
            id = "cat-1",
            code = "anime_lovers",
            sectionGroup = ExploreSectionGroup.OTAKU,
            iconKey = "sparkles",
            title = "Amantes del Anime",
            description = "Descubre gente con tus mismos gustos",
            activeCount = 10,
            radiusKm = 50,
        )

    private val sampleHubData =
        ExploreHubData(
            recommendations = listOf(sampleCategory),
            sections = listOf(
                ExploreSection(
                    group = ExploreSectionGroup.OTAKU,
                    title = "Otaku",
                    categories = listOf(sampleCategory),
                ),
            ),
        )

    @Before
    fun setUp() {
        getExploreSectionsUseCase = mockk()
    }

    @Test
    fun loadExploreHub_emitsSuccessState_whenUseCaseSucceeds() = runTest {
        coEvery { getExploreSectionsUseCase() } returns MiraiLinkResult.Success(sampleHubData)

        viewModel = ExploreViewModel(
            getExploreSectionsUseCase = getExploreSectionsUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ExploreViewModel.ExploreUiState.Success)
        assertEquals(sampleHubData, (state as ExploreViewModel.ExploreUiState.Success).hubData)
    }

    @Test
    fun loadExploreHub_emitsErrorState_whenUseCaseFails() = runTest {
        coEvery { getExploreSectionsUseCase() } returns MiraiLinkResult.Error(UnknownError)

        viewModel = ExploreViewModel(
            getExploreSectionsUseCase = getExploreSectionsUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
        )

        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is ExploreViewModel.ExploreUiState.Error)
    }

    @Test
    fun performErrorAction_retriesLoadingExploreHub() = runTest {
        coEvery { getExploreSectionsUseCase() } returnsMany listOf(
            MiraiLinkResult.Error(UnknownError),
            MiraiLinkResult.Success(sampleHubData),
        )

        viewModel = ExploreViewModel(
            getExploreSectionsUseCase = getExploreSectionsUseCase,
            ioDispatcher = mainCoroutineRule.testDispatcher,
        )

        advanceUntilIdle()
        assertTrue(viewModel.state.value is ExploreViewModel.ExploreUiState.Error)

        viewModel.performErrorAction()
        advanceUntilIdle()

        assertTrue(viewModel.state.value is ExploreViewModel.ExploreUiState.Success)
        coVerify(exactly = 2) { getExploreSectionsUseCase() }
    }
}
