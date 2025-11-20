// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.request.report.ReportUserRequest
import com.feryaeljustice.mirailink.data.remote.ReportApiService
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject
import retrofit2.Response

@ExperimentalCoroutinesApi
class ReportRemoteDataSourceTest : UnitTest() {

    private val reportApiService: ReportApiService by inject()
    private val reportRemoteDataSource: ReportRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<ReportApiService>() }
                single { ReportRemoteDataSource(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `reportUser should return success`() = runTest {
        // Given
        val reportedUser = "user123"
        val reason = "Spam"
        val request = ReportUserRequest(reportedUser, reason)
        coEvery { reportApiService.reportUser(request) } returns Response.success(Unit)

        // When
        val result = reportRemoteDataSource.reportUser(reportedUser, reason)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { reportApiService.reportUser(request) }
    }
}
