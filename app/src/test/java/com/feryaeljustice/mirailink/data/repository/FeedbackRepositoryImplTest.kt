// Feryael Justice
// 2024-07-31

package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.FeedbackRemoteDatasource
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class FeedbackRepositoryImplTest {

    private lateinit var feedbackRepository: FeedbackRepositoryImpl
    private val remoteDataSource: FeedbackRemoteDatasource = mockk()

    @Before
    fun setUp() {
        feedbackRepository = FeedbackRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `sendFeedback returns success when remote data source is successful`() = runTest {
        // Given
        val feedback = "This is a test feedback."
        val successResult = MiraiLinkResult.Success(Unit)
        coEvery { remoteDataSource.sendFeedback(feedback) } returns successResult

        // When
        val result = feedbackRepository.sendFeedback(feedback)

        // Then
        assertThat(result).isEqualTo(successResult)
    }

    @Test
    fun `sendFeedback returns error when remote data source fails`() = runTest {
        // Given
        val feedback = "This is a test feedback."
        val errorResult = MiraiLinkResult.Error("An error occurred")
        coEvery { remoteDataSource.sendFeedback(feedback) } returns errorResult

        // When
        val result = feedbackRepository.sendFeedback(feedback)

        // Then
        assertThat(result).isEqualTo(errorResult)
    }
}