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
class CreateGroupChatUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var createGroupChatUseCase: CreateGroupChatUseCase

    @Before
    fun onBefore() {
        createGroupChatUseCase = CreateGroupChatUseCase(repository)
    }

    @Test
    fun `when repository creates group chat successfully, return success with chat id`() = runTest {
        // Given
        val name = "Test Group"
        val userIds = listOf("user1", "user2")
        val chatId = "newChatId"
        coEvery {
            repository.createGroupChat(
                name,
                userIds
            )
        } returns MiraiLinkResult.Success(chatId)

        // When
        val result = createGroupChatUseCase(name, userIds)

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(chatId, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository fails to create group chat, return error`() = runTest {
        // Given
        val name = "Test Group"
        val userIds = listOf("user1", "user2")
        val errorResult = MiraiLinkResult.Error("Error creating group chat")
        coEvery { repository.createGroupChat(name, userIds) } returns errorResult

        // When
        val result = createGroupChatUseCase(name, userIds)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val name = "Test Group"
        val userIds = listOf("user1", "user2")
        val exception = RuntimeException("Network error")
        coEvery { repository.createGroupChat(name, userIds) } throws exception

        // When
        val result = createGroupChatUseCase(name, userIds)

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while creating the group chat",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}