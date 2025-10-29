/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetChatMessagesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var getChatMessagesUseCase: GetChatMessagesUseCase

    @Before
    fun onBefore() {
        getChatMessagesUseCase = GetChatMessagesUseCase(repository)
    }

    @Test
    fun `when repository returns a list of messages, return success with the list`() = runTest {
        // Given
        val userId = "userId"
        val messages = listOf(mockk<ChatMessage>(), mockk<ChatMessage>())
        coEvery { repository.getMessagesWith(userId) } returns MiraiLinkResult.Success(messages)

        // When
        val result = getChatMessagesUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(messages, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val userId = "userId"
        val messages = emptyList<ChatMessage>()
        coEvery { repository.getMessagesWith(userId) } returns MiraiLinkResult.Success(messages)

        // When
        val result = getChatMessagesUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val userId = "userId"
        val errorResult = MiraiLinkResult.Error("Error getting messages")
        coEvery { repository.getMessagesWith(userId) } returns errorResult

        // When
        val result = getChatMessagesUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val userId = "userId"
        val exception = RuntimeException("Network error")
        coEvery { repository.getMessagesWith(userId) } throws exception

        // When
        val result = getChatMessagesUseCase(userId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while getting the messages",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}