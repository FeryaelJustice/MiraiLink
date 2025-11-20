// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
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
class ReportRepositoryImplTest : UnitTest() {
    private val reportRepository: ReportRepositoryImpl by inject()
    private val remoteDataSource: ReportRemoteDataSource by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            module {
                single { mockk<ReportRemoteDataSource>() }
                single { ReportRepositoryImpl(get()) }
            },
        )
    }

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `reportUser returns success when remote data source is successful`() =
        runTest {
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
    fun `reportUser returns error when remote data source fails`() =
        runTest {
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
