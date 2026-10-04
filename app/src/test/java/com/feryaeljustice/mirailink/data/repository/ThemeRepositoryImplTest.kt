package com.feryaeljustice.mirailink.data.repository

import app.cash.turbine.test
import com.feryaeljustice.mirailink.data.datastore.MiraiLinkPrefs
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeRepositoryImplTest {
    private val miraiLinkPrefs = mockk<MiraiLinkPrefs>(relaxed = true)
    private val repository = ThemeRepositoryImpl(miraiLinkPrefs)

    @Test
    fun `getThemePreference emits preferences from miraiLinkPrefs`() = runTest {
        every { miraiLinkPrefs.getThemePreference() } returns flowOf(ThemePreference.DARK)

        repository.getThemePreference().test {
            assertEquals(ThemePreference.DARK, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `setThemePreference forwards call to miraiLinkPrefs`() = runTest {
        repository.setThemePreference(ThemePreference.LIGHT)

        coVerify { miraiLinkPrefs.setThemePreference(ThemePreference.LIGHT) }
    }
}
