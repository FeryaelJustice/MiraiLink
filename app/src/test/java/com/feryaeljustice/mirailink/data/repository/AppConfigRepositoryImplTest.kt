/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource
import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AppConfigRepositoryImplTest {

    private val remote: AppConfigRemoteDataSource = mockk()
    private val repository = AppConfigRepositoryImpl(remote)

    @Test
    fun `getVersion returns success`() = runBlocking {
        // Given
        val appVersionInfo = AppVersionInfo(
            platform = "Android",
            minVersionCode = 1,
            latestVersionCode = 2,
            message = "Update available",
            playStoreUrl = "http://play.store.url"
        )
        val expectedResult = MiraiLinkResult.Success(appVersionInfo)
        coEvery { remote.getVersion() } returns expectedResult

        // When
        val result = repository.getVersion()

        // Then
        assertEquals(expectedResult, result)
    }

    @Test
    fun `getVersion returns error`() = runBlocking {
        // Given
        val expectedResult = MiraiLinkResult.Error("error")
        coEvery { remote.getVersion() } returns expectedResult

        // When
        val result = repository.getVersion()

        // Then
        assertEquals(expectedResult, result)
    }
}