// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class OnboardingRepositoryImplTest : UnitTest() {
    private val onboardingRepository: OnboardingRepositoryImpl by inject()
    private val miraiLinkPrefs: MiraiLinkPrefs by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<MiraiLinkPrefs>() }
                single { OnboardingRepositoryImpl(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `checkOnboardingIsCompleted returns success when prefs returns true`() =
        runTest {
            // Given
            coEvery { miraiLinkPrefs.isOnboardingCompleted() } returns true

            // When
            val result = onboardingRepository.checkOnboardingIsCompleted()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            assertThat((result as MiraiLinkResult.Success).data).isTrue()
        }

    @Test
    fun `checkOnboardingIsCompleted returns success when prefs returns false`() =
        runTest {
            // Given
            coEvery { miraiLinkPrefs.isOnboardingCompleted() } returns false

            // When
            val result = onboardingRepository.checkOnboardingIsCompleted()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            assertThat((result as MiraiLinkResult.Success).data).isFalse()
        }

    @Test
    fun `checkOnboardingIsCompleted returns error when prefs throws exception`() =
        runTest {
            // Given
            val exception = RuntimeException("A wild error appears!")
            coEvery { miraiLinkPrefs.isOnboardingCompleted() } throws exception

            // When
            val result = onboardingRepository.checkOnboardingIsCompleted()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Error::class.java)
            val error = result as MiraiLinkResult.Error
            assertThat(error.exception).isEqualTo(exception)
        }
}
