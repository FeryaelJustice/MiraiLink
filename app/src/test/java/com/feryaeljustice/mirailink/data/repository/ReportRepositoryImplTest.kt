// Feryael Justice
// 2024-07-31

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ReportRepositoryImplTest {

    private lateinit var reportRepository: ReportRepositoryImpl
    private val remoteDataSource: ReportRemoteDataSource = mockk()

    @Before
    fun setUp() {
        reportRepository = ReportRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `reportUser returns success when remote data source is successful`() = runTest {
        // Given
        val reportedUser = "user123"
        val reason = "Spam"
        val successResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.reportUser(reportedUser, reason) } returns successResult

        // When
        val result = reportRepository.reportUser(reportedUser, reason)

        // Then
        assertThat(result).isEqualTo(successResult)
    }

    @Test
    fun `reportUser returns error when remote data source fails`() = runTest {
        // Given
        val reportedUser = "user123"
        val reason = "Spam"
        val errorResult = MiraiLinkResult.Error("An error occurred")
        coEvery { remoteDataSource.reportUser(reportedUser, reason) } returns errorResult

        // When
        val result = reportRepository.reportUser(reportedUser, reason)

        // Then
        assertThat(result).isEqualTo(errorResult)
    }
}