package com.feryaeljustice.mirailink.domain.usecase.settings

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class SearchPreferencesUseCaseTest : UnitTest() {
    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase by inject()
    private val saveSearchPreferencesUseCase: SaveSearchPreferencesUseCase by inject()
    private val repository: SearchPreferencesRepository by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<SearchPreferencesRepository>() }
                    single { GetSearchPreferencesUseCase(get()) }
                    single { SaveSearchPreferencesUseCase(get()) }
                },
            )
        }

    @Test
    fun `getSearchPreferences returns flow from repository`() = runTest {
        val expected = SearchPreferences(radiusKm = 60f, scope = SearchScope.MY_COUNTRY)
        coEvery { repository.getSearchPreferences() } returns flowOf(expected)

        val result = getSearchPreferencesUseCase().first()

        assertEquals(60f, result.radiusKm, 0.01f)
        assertEquals(SearchScope.MY_COUNTRY, result.scope)
    }

    @Test
    fun `saveSearchPreferences saves successfully`() = runTest {
        val prefs = SearchPreferences(radiusKm = 25f, matchByLiveLocation = true)
        coEvery { repository.saveSearchPreferences(prefs) } returns MiraiLinkResult.Success(Unit)

        val result = saveSearchPreferencesUseCase(prefs)

        assertTrue(result is MiraiLinkResult.Success)
    }
}
