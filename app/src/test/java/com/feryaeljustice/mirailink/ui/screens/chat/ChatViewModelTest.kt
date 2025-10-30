// Author: Feryael Justice
// Date: 2024-07-29

package com.feryaeljustice.mirailink.ui.screens.chat

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.MarkChatAsReadUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase
import com.feryaeljustice.mirailink.domain.usecase.report.ReportUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ChatViewModel
    private val createPrivateChatUseCase: Lazy<CreatePrivateChatUseCase> = mockk()
    private val createGroupChatUseCase: Lazy<CreateGroupChatUseCase> = mockk()
    private val getChatMessagesUseCase: Lazy<GetChatMessagesUseCase> = mockk()
    private val markChatAsReadUseCase: Lazy<MarkChatAsReadUseCase> = mockk()
    private val sendMessageUseCase: Lazy<SendMessageUseCase> = mockk()
    private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase> = mockk()
    private val getUserByIdUseCase: Lazy<GetUserByIdUseCase> = mockk()
    private val reportUseCase: Lazy<ReportUseCase> = mockk()

    @Before
    fun setUp() {
        viewModel =
            ChatViewModel(
                createPrivateChatUseCase,
                createGroupChatUseCase,
                getChatMessagesUseCase,
                markChatAsReadUseCase,
                sendMessageUseCase,
                getCurrentUserUseCase,
                getUserByIdUseCase,
                reportUseCase,
            )
    }

    @Test
    fun `init private chat success`() =
        runTest {
            val receiverId = "receiverId"
            val chatId = "chatId"
            val sender =
                User("senderId", "sender", "sender", "sender@test.com", null, null, null, null, emptyList(), emptyList(), emptyList())
            val receiver =
                User(receiverId, "receiver", "receiver", "receiver@test.com", null, null, null, null, emptyList(), emptyList(), emptyList())

            coEvery { createPrivateChatUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success(chatId)
            coEvery { getCurrentUserUseCase.get().invoke() } returns MiraiLinkResult.Success(sender)
            coEvery { getUserByIdUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success(receiver)
            coEvery { markChatAsReadUseCase.get().invoke(chatId) } returns MiraiLinkResult.Success(Unit)
            coEvery { getChatMessagesUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success(emptyList())

            viewModel.initChat(receiverId = receiverId, type = ChatViewModel.Companion.CHATTYPE.PRIVATE)

            assert(viewModel.chatId.value == chatId)
            assert(viewModel.sender.value?.id == sender.id)
            assert(viewModel.receiver.value?.id == receiver.id)
        }

    @Test
    fun `send message success`() =
        runTest {
            val receiverId = "receiverId"
            val sender =
                User("senderId", "sender", "sender", "sender@test.com", null, null, null, null, emptyList(), emptyList(), emptyList())
            val receiver =
                User(receiverId, "receiver", "receiver", "receiver@test.com", null, null, null, null, emptyList(), emptyList(), emptyList())
            val message = "Hello"

            // Simulate that the chat is already initialized
            coEvery { createPrivateChatUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success("chatId")
            coEvery { getCurrentUserUseCase.get().invoke() } returns MiraiLinkResult.Success(sender)
            coEvery { getUserByIdUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success(receiver)
            coEvery { markChatAsReadUseCase.get().invoke("chatId") } returns MiraiLinkResult.Success(Unit)
            coEvery { getChatMessagesUseCase.get().invoke(receiverId) } returns MiraiLinkResult.Success(emptyList())
            viewModel.initChat(receiverId = receiverId, type = ChatViewModel.Companion.CHATTYPE.PRIVATE)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            coEvery { sendMessageUseCase.get().invoke(receiverId, message) } returns MiraiLinkResult.Success(Unit)

            viewModel.sendMessage(message)
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            assert(viewModel.messages.value.any { it.content == message })
        }
}
