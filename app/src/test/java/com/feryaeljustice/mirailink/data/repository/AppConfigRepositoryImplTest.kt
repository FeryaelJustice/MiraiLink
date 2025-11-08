// Feryael Justice
// 2025-11-08

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.AppConfigRemoteDataSource
import com.feryaeljustice.mirailink.domain.model.AppVersionInfo
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AppConfigRepositoryImplTest {
    private lateinit var appConfigRepository: AppConfigRepositoryImpl
    private val appConfigRemoteDataSource: AppConfigRemoteDataSource = mockk()

    @Before
    fun setUp() {
        appConfigRepository = AppConfigRepositoryImpl(appConfigRemoteDataSource)
    }

    @Test
    fun `getVersion returns success when remote data source is successful`() =
        runTest {
            // Given
            val appVersionInfo =
                AppVersionInfo(
                    platform = "android",
                    minVersionCode = 1,
                    latestVersionCode = 2,
                    message = "Update available",
                    playStoreUrl = "https://play.google.com",
                )
            val successResult = MiraiLinkResult.Success(appVersionInfo)
            coEvery { appConfigRemoteDataSource.getVersion() } returns successResult

            // When
            val result = appConfigRepository.getVersion()

            // Then
            assertThat(result).isEqualTo(successResult)
        }

    @Test
    fun `getVersion returns error when remote data source fails`() =
        runTest {
            // Given
            val errorResult = MiraiLinkResult.Error(message = "An error occurred")
            coEvery { appConfigRemoteDataSource.getVersion() } returns errorResult

            // When
            val result = appConfigRepository.getVersion()

            // Then
            assertThat(result).isEqualTo(errorResult)
        }
}
