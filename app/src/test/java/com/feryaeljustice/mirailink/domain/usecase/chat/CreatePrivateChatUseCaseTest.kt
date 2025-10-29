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
class CreatePrivateChatUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var createPrivateChatUseCase: CreatePrivateChatUseCase

    @Before
    fun onBefore() {
        createPrivateChatUseCase = CreatePrivateChatUseCase(repository)
    }

    @Test
    fun `when repository creates private chat successfully, return success with chat id`() =
        runTest {
            // Given
            val otherUserId = "otherUserId"
            val chatId = "newChatId"
            coEvery { repository.createPrivateChat(otherUserId) } returns MiraiLinkResult.Success(
                chatId
            )

            // When
            val result = createPrivateChatUseCase(otherUserId)

            // Then
            assertTrue(result is MiraiLinkResult.Success)
            assertEquals(chatId, (result as MiraiLinkResult.Success).data)
        }

    @Test
    fun `when repository fails to create private chat, return error`() = runTest {
        // Given
        val otherUserId = "otherUserId"
        val errorResult = MiraiLinkResult.Error("Error creating private chat")
        coEvery { repository.createPrivateChat(otherUserId) } returns errorResult

        // When
        val result = createPrivateChatUseCase(otherUserId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val otherUserId = "otherUserId"
        val exception = RuntimeException("Network error")
        coEvery { repository.createPrivateChat(otherUserId) } throws exception

        // When
        val result = createPrivateChatUseCase(otherUserId)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while creating the private chat",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}