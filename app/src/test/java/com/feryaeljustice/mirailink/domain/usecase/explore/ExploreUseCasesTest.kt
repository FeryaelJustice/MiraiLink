package com.feryaeljustice.mirailink.domain.usecase.explore

import com.feryaeljustice.mirailink.domain.model.explore.CategoryPreference
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSection
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.repository.ExploreHubData
import com.feryaeljustice.mirailink.domain.repository.ExploreRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExploreUseCasesTest {
    private lateinit var repository: ExploreRepository
    private lateinit var getExploreSectionsUseCase: GetExploreSectionsUseCase
    private lateinit var getCategoryFeedUseCase: GetCategoryFeedUseCase
    private lateinit var getCategoryPreferencesUseCase: GetCategoryPreferencesUseCase
    private lateinit var updateCategoryPreferencesUseCase: UpdateCategoryPreferencesUseCase

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

    private val sampleUser =
        User(
            id = "user-1",
            username = "naruto",
            nickname = "Naruto",
            email = "naruto@leaf.com",
            phoneNumber = null,
            bio = "Hokage",
            gender = "male",
            birthdate = "2000-01-01",
            games = emptyList(),
            animes = emptyList(),
        )

    private val samplePreference =
        CategoryPreference(
            categoryId = "cat-1",
            radiusKm = 100,
        )

    @Before
    fun setUp() {
        repository = mockk()
        getExploreSectionsUseCase = GetExploreSectionsUseCase(repository)
        getCategoryFeedUseCase = GetCategoryFeedUseCase(repository)
        getCategoryPreferencesUseCase = GetCategoryPreferencesUseCase(repository)
        updateCategoryPreferencesUseCase = UpdateCategoryPreferencesUseCase(repository)
    }

    @Test
    fun getExploreSectionsUseCase_delegatesToRepositorySuccessfully() = runTest {
        coEvery { repository.getExploreHubData() } returns MiraiLinkResult.Success(sampleHubData)

        val result = getExploreSectionsUseCase()

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(sampleHubData, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.getExploreHubData() }
    }

    @Test
    fun getCategoryFeedUseCase_delegatesToRepositoryWithParams() = runTest {
        coEvery { repository.getCategoryFeed("cat-1", 20, 0) } returns MiraiLinkResult.Success(listOf(sampleUser))

        val result = getCategoryFeedUseCase("cat-1", 20, 0)

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(listOf(sampleUser), (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.getCategoryFeed("cat-1", 20, 0) }
    }

    @Test
    fun getCategoryPreferencesUseCase_delegatesToRepository() = runTest {
        coEvery { repository.getCategoryPreferences("cat-1") } returns MiraiLinkResult.Success(samplePreference)

        val result = getCategoryPreferencesUseCase("cat-1")

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(samplePreference, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.getCategoryPreferences("cat-1") }
    }

    @Test
    fun updateCategoryPreferencesUseCase_delegatesToRepositoryWithRadius() = runTest {
        coEvery { repository.updateCategoryPreferences("cat-1", 120) } returns MiraiLinkResult.Success(samplePreference.copy(radiusKm = 120))

        val result = updateCategoryPreferencesUseCase("cat-1", 120)

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(120, (result as MiraiLinkResult.Success).data.radiusKm)
        coVerify(exactly = 1) { repository.updateCategoryPreferences("cat-1", 120) }
    }
}
