/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OnboardingRepositoryImplTest {

    private lateinit var miraiLinkPrefs: MiraiLinkPrefs
    private lateinit var repository: OnboardingRepositoryImpl

    @Before
    fun setUp() {
        miraiLinkPrefs = mockk()
        repository = OnboardingRepositoryImpl(miraiLinkPrefs)
    }

    @Test
    fun `checkOnboardingIsCompleted returns value from prefs`() = runBlocking {
        // Given
        val expectedValue = true
        coEvery { miraiLinkPrefs.isOnboardingCompleted() } returns expectedValue

        // When
        val result = repository.checkOnboardingIsCompleted()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(expectedValue, (result as MiraiLinkResult.Success).data)
    }
}