package com.feryaeljustice.mirailink.domain.usecase

import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.repository.AppConfigRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CheckAppVersionUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repo: AppConfigRepository

    private lateinit var checkAppVersionUseCase: CheckAppVersionUseCase

    @Before
    fun onBefore() {
        checkAppVersionUseCase = CheckAppVersionUseCase(repo)
    }

    @Test
    fun `when current version is lower than min version, mustUpdate should be true`() = runTest {
        // Given
        val currentVersion = 1
        val serverVersionInfo = AppVersionInfo(
            platform = "android",
            minVersionCode = 5,
            latestVersionCode = 10,
            message = "Update required!",
            playStoreUrl = "url"
        )
        coEvery { repo.getVersion() } returns MiraiLinkResult.Success(serverVersionInfo)

        // When
        val result = checkAppVersionUseCase(currentVersion)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val versionCheckResult = (result as MiraiLinkResult.Success).data
        assertTrue(versionCheckResult.mustUpdate)
        assertFalse(versionCheckResult.shouldUpdate)
        assertEquals("Update required!", versionCheckResult.message)
    }

    @Test
    fun `when current version is lower than latest version but not min version, shouldUpdate should be true`() =
        runTest {
            // Given
            val currentVersion = 6
            val serverVersionInfo = AppVersionInfo(
                platform = "android",
                minVersionCode = 5,
                latestVersionCode = 10,
                message = "Update available",
                playStoreUrl = "url"
            )
            coEvery { repo.getVersion() } returns MiraiLinkResult.Success(serverVersionInfo)

            // When
            val result = checkAppVersionUseCase(currentVersion)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            val versionCheckResult = (result as MiraiLinkResult.Success).data
            assertFalse(versionCheckResult.mustUpdate)
            assertTrue(versionCheckResult.shouldUpdate)
            assertEquals("Update available", versionCheckResult.message)
        }

    @Test
    fun `when current version is up to date, mustUpdate and shouldUpdate should be false`() =
        runTest {
            // Given
            val currentVersion = 10
            val serverVersionInfo = AppVersionInfo(
                platform = "android",
                minVersionCode = 5,
                latestVersionCode = 10,
                message = "App is up to date",
                playStoreUrl = "url"
            )
            coEvery { repo.getVersion() } returns MiraiLinkResult.Success(serverVersionInfo)

            // When
            val result = checkAppVersionUseCase(currentVersion)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            val versionCheckResult = (result as MiraiLinkResult.Success).data
            assertFalse(versionCheckResult.mustUpdate)
            assertFalse(versionCheckResult.shouldUpdate)
        }

    @Test
    fun `when current version is higher than latest version, mustUpdate and shouldUpdate should be false`() =
        runTest {
            // Given
            val currentVersion = 11
            val serverVersionInfo = AppVersionInfo(
                platform = "android",
                minVersionCode = 5,
                latestVersionCode = 10,
                message = "App is up to date",
                playStoreUrl = "url"
            )
            coEvery { repo.getVersion() } returns MiraiLinkResult.Success(serverVersionInfo)

            // When
            val result = checkAppVersionUseCase(currentVersion)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            val versionCheckResult = (result as MiraiLinkResult.Success).data
            assertFalse(versionCheckResult.mustUpdate)
            assertFalse(versionCheckResult.shouldUpdate)
        }

    @Test
    fun `when repository returns error, use case should return error`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { repo.getVersion() } returns MiraiLinkResult.Error(errorMessage)

        // When
        val result = checkAppVersionUseCase(1)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        val error = result as MiraiLinkResult.Error
        assertEquals(errorMessage, error.message)
    }

    @Test
    fun `when server message is null, message should be empty`() = runTest {
        // Given
        val serverVersionInfo = AppVersionInfo(
            platform = "android",
            minVersionCode = 5,
            latestVersionCode = 10,
            message = null,
            playStoreUrl = "url"
        )
        coEvery { repo.getVersion() } returns MiraiLinkResult.Success(serverVersionInfo)

        // When
        val result = checkAppVersionUseCase(1)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        val versionCheckResult = (result as MiraiLinkResult.Success).data
        assertTrue(versionCheckResult.message.isEmpty())
    }
}