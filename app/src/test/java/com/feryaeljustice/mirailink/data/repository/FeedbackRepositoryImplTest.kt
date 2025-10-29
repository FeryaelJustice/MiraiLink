/**
 * @author Feryael Justice
 * @since 1/11/2024
 */
package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FeedbackRepositoryImplTest {

    private lateinit var remoteDataSource: FeedbackRemoteDatasource
    private lateinit var repository: FeedbackRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repository = FeedbackRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `sendFeedback calls remote and returns result`() = runBlocking {
        // Given
        val feedback = "This is a test feedback."
        val expectedResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.sendFeedback(feedback) } returns expectedResult

        // When
        val result = repository.sendFeedback(feedback)

        // Then
        assertEquals(expectedResult, result)
    }
}