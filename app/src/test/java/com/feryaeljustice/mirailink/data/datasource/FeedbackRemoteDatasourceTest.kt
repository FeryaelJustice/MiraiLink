// Author: Feryael Justice
// Date: 2025-11-01

package com.feryaeljustice.mirailink.data.datasource

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.model.request.feedback.SendFeedbackRequest
import com.feryaeljustice.mirailink.data.remote.FeedbackApiService
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
class FeedbackRemoteDatasourceTest : UnitTest() {

    private lateinit var feedbackApiService: FeedbackApiService
    private lateinit var feedbackRemoteDatasource: FeedbackRemoteDatasource

    @Before
    override fun setUp() {
        super.setUp()
        feedbackApiService = mockk()
        feedbackRemoteDatasource = FeedbackRemoteDatasource(feedbackApiService)
    }

    @Test
    fun `sendFeedback should return success`() = runTest {
        // Given
        val feedback = "This is a test feedback."
        val request = SendFeedbackRequest(feedback)
        coEvery { feedbackApiService.sendFeeback(request) } returns Response.success(Unit)

        // When
        val result = feedbackRemoteDatasource.sendFeedback(feedback)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        coVerify { feedbackApiService.sendFeeback(request) }
    }
}