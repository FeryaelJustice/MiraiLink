package com.feryaeljustice.mirailink.ui.screens.home.search

import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.repository.CapsuleRepository
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CapsuleModeViewModelTest {
    @get:Rule
    val mainCoroutineRule = com.feryaeljustice.mirailink.util.MainCoroutineRule()

    private val capsuleRepository: CapsuleRepository = mockk()
    private val preferencesRepository: SearchPreferencesRepository = mockk()

    @Test
    fun `refresh clears stale availability and capsule selection is blocked until the new result`() = runTest {
        val currentAvailability = CompletableDeferred<Boolean>()
        var availabilityCalls = 0
        every { preferencesRepository.getSearchPreferences() } returns flowOf(SearchPreferences())
        coEvery { capsuleRepository.available() } coAnswers {
            if (availabilityCalls++ == 0) true else currentAvailability.await()
        }
        coEvery { preferencesRepository.saveSearchPreferences(any()) } returns MiraiLinkResult.Success(Unit)

        val viewModel = CapsuleModeViewModel(capsuleRepository, preferencesRepository)
        viewModel.refresh()
        advanceUntilIdle()
        assertTrue(viewModel.available.value)

        viewModel.refresh()
        assertTrue(viewModel.availabilityLoading.value)
        assertFalse(viewModel.available.value)
        viewModel.select("capsule")
        runCurrent()
        coVerify(exactly = 0) { preferencesRepository.saveSearchPreferences(any()) }

        currentAvailability.complete(false)
        advanceUntilIdle()
        assertFalse(viewModel.availabilityLoading.value)
        assertFalse(viewModel.available.value)
        coVerify(exactly = 0) { preferencesRepository.saveSearchPreferences(any()) }
    }
}
