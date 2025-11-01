// Author: Feryael Justice
// Date: 2025-11-01

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
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ReportRemoteDataSourceTest : UnitTest() {

    private lateinit var reportApiService: ReportApiService
    private lateinit var reportRemoteDataSource: ReportRemoteDataSource

    @Before
    override fun setUp() {
        super.setUp()
        reportApiService = mockk()
        reportRemoteDataSource = ReportRemoteDataSource(reportApiService)
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