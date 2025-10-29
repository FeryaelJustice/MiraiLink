/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coJustRun
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
class DisconnectSocketUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var disconnectSocketUseCase: DisconnectSocketUseCase

    @Before
    fun onBefore() {
        disconnectSocketUseCase = DisconnectSocketUseCase(repository)
    }

    @Test
    fun `when repository disconnects successfully, return success`() = runTest {
        // Given
        coJustRun { repository.disconnectSocket() }

        // When
        val result = disconnectSocketUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Success<*>)
    }

    @Test
    fun `when repository throws an exception, return error`() = runTest {
        // Given
        val exception = RuntimeException("Disconnection failed")
        coEvery { repository.disconnectSocket() } throws exception

        // When
        val result = disconnectSocketUseCase()

        // Then
        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(
            "An error occurred while disconnecting from the socket",
            (result as MiraiLinkResult.Error).message
        )
        assertEquals(exception, result.exception)
    }
}