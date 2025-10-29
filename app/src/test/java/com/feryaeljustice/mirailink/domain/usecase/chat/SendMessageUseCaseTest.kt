/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SendMessageUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Before
    fun onBefore() {
        sendMessageUseCase = SendMessageUseCase(repository)
    }

    @Test
    fun `when repository sends message successfully, return success`() = runTest {
        // Given
        val userId = "userId"
        val message = "Hello"
        coEvery { repository.sendMessageTo(userId, message) } returns MiraiLinkResult.Success(Unit)

        // When
        val result = sendMessageUseCase(userId, message)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
    }

    @Test
    fun `when repository fails to send message, return error`() = runTest {
        // Given
        val userId = "userId"
        val message = "Hello"
        val errorResult = MiraiLinkResult.Error("Error sending message")
        coEvery { repository.sendMessageTo(userId, message) } returns errorResult

        // When
        val result = sendMessageUseCase(userId, message)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val userId = "userId"
        val message = "Hello"
        val exception = RuntimeException("Network error")
        coEvery { repository.sendMessageTo(userId, message) } throws exception

        // When
        val result = sendMessageUseCase(userId, message)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while sending the message",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}