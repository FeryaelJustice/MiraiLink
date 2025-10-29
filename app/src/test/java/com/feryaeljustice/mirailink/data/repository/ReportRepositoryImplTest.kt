/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.ReportRemoteDataSource
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReportRepositoryImplTest {

    private lateinit var remoteDataSource: ReportRemoteDataSource
    private lateinit var repository: ReportRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = ReportRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `reportUser calls remote and returns result`() = runBlocking {
        // Given
        val reportedUser = "user1"
        val reason = "spam"
        val expectedResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.reportUser(reportedUser, reason) } returns expectedResult

        // When
        val result = repository.reportUser(reportedUser, reason)

        // Then
        assertEquals(expectedResult, result)
    }
}