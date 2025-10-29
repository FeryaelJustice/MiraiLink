/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
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
class GetChatsFromUserTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var getChatsFromUser: GetChatsFromUser

    @Before
    fun onBefore() {
        getChatsFromUser = GetChatsFromUser(repository)
    }

    @Test
    fun `when repository returns a list of chats, return success with the list`() = runTest {
        // Given
        val chats = listOf(mockk<ChatSummary>(), mockk<ChatSummary>())
        coEvery { repository.getChatsFromUser() } returns MiraiLinkResult.Success(chats)

        // When
        val result = getChatsFromUser()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(chats, (result as MiraiLinkResult.Success).data)
    }

    @Test
    fun `when repository returns an empty list, return success with an empty list`() = runTest {
        // Given
        val chats = emptyList<ChatSummary>()
        coEvery { repository.getChatsFromUser() } returns MiraiLinkResult.Success(chats)

        // When
        val result = getChatsFromUser()

        // Then
        assertTrue(result is MiraiLinkResult.Success)
        assertTrue((result as MiraiLinkResult.Success).data.isEmpty())
    }

    @Test
    fun `when repository fails, return error`() = runTest {
        // Given
        val errorResult = MiraiLinkResult.Error("Error getting chats")
        coEvery { repository.getChatsFromUser() } returns errorResult

        // When
        val result = getChatsFromUser()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(errorResult.message, (result as MiraiLinkResult.Error).message)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { repository.getChatsFromUser() } throws exception

        // When
        val result = getChatsFromUser()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while getting the chats",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}