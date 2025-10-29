/**
 * @author Feryael Justice
 * @since 31/10/2024
 */
package com.feryaeljustice.mirailink.domain.usecase.chat

import com.feryaeljustice.mirailink.domain.repository.ChatRepository
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ListenForMessagesUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var repository: ChatRepository

    private lateinit var listenForMessagesUseCase: ListenForMessagesUseCase

    @Before
    fun onBefore() {
        listenForMessagesUseCase = ListenForMessagesUseCase(repository)
    }

    @Test
    fun `when use case is invoked, listenForMessages should be called`() {
        // Given
        val callback: (String) -> Unit = {}

        // When
        listenForMessagesUseCase(callback)

        // Then
        verify(exactly = 1) { repository.listenForMessages(callback) }
    }
}