// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.data.model.AppVersionInfoDto
import com.feryaeljustice.mirailink.data.remote.AppConfigApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class AppConfigRemoteDataSourceTest : UnitTest() {

    private val appConfigApiService: AppConfigApiService by inject()
    private val appConfigRemoteDataSource: AppConfigRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<AppConfigApiService>() }
                single { AppConfigRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getVersion should return app version info on success`() = runTest {
        // Given
        val appVersionInfoDto = AppVersionInfoDto(
            platform = "android",
            minVersionCode = 1,
            latestVersionCode = 2,
            message = "Update available",
            playStoreUrl = "url",
        )
        coEvery { appConfigApiService.getAndroidAppVersion() } returns appVersionInfoDto

        // When
        val result = appConfigRemoteDataSource.getVersion()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(appVersionInfoDto.toDomain(), (result as MiraiLinkResult.Success).data)
        coVerify { appConfigApiService.getAndroidAppVersion() }
    }
}
