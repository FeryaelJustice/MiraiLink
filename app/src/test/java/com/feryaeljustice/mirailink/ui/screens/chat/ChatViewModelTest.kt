/**
 * @author Feryael Justice
 * @date 01/08/2024
 */
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
import com.feryaeljustice.mirailink.domain.util.Logger
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import dagger.Lazy
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ChatViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Mocks
    private val createPrivateChatUseCase: Lazy<CreatePrivateChatUseCase> = mockk()
    private val createGroupChatUseCase: Lazy<CreateGroupChatUseCase> = mockk()
    private val getChatMessagesUseCase: Lazy<GetChatMessagesUseCase> = mockk()
    private val markChatAsReadUseCase: Lazy<MarkChatAsReadUseCase> = mockk()
    private val sendMessageUseCase: Lazy<SendMessageUseCase> = mockk()
    private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase> = mockk()
    private val getUserByIdUseCase: Lazy<GetUserByIdUseCase> = mockk()
    private val reportUseCase: Lazy<ReportUseCase> = mockk()
    private val logger: Logger = mockk(relaxed = true)

    // ViewModel
    private lateinit var viewModel: ChatViewModel

    // Test Data
    private val sender =
        User(
            "senderId",
            "sender",
            "sender",
            "sender@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    private val receiver =
        User(
            "receiverId",
            "receiver",
            "receiver",
            "receiver@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    private val chatId = "chatId"
    private val receiverId = "receiverId"

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
                logger,
                mainCoroutineRule.testDispatcher,
            )
    }

    @After
    fun tearDown() {
        viewModel.stopMessagePolling()
    }

    private fun prepareInitializedChatMocks() {
        coEvery {
            createPrivateChatUseCase.get().invoke(receiverId)
        } returns MiraiLinkResult.Success(chatId)
        coEvery { getCurrentUserUseCase.get().invoke() } returns MiraiLinkResult.Success(sender)
        coEvery { getUserByIdUseCase.get().invoke(receiverId) } returns
            MiraiLinkResult.Success(
                receiver,
            )
        coEvery { markChatAsReadUseCase.get().invoke(chatId) } returns MiraiLinkResult.Success(Unit)
        // da igual lo que devuelva, pero que no falle
        coEvery { getChatMessagesUseCase.get().invoke(any()) } returns
            MiraiLinkResult.Success(
                emptyList(),
            )
    }

    @Test
    fun `init private chat success`() =
        runTest {
            // Arrange
            prepareInitializedChatMocks()

            // Act
            viewModel.initChat(
                receiverId = receiverId,
                type = ChatViewModel.Companion.CHATTYPE.PRIVATE,
            )

            // 1️⃣ deja que se ejecute el launch de initChat y lo que haya dentro
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // 2️⃣ ahora sí: ya se habrá arrancado el polling -> lo paramos
            viewModel.stopMessagePolling()

            // (si tu init hace más cosas después, puedes volver a drenar)
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // Assert
            assert(viewModel.chatId.value == chatId)
            assert(viewModel.sender.value?.id == sender.id)
            assert(viewModel.receiver.value?.id == receiver.id)
        }

    @Test
    fun `send message success`() =
        runTest {
            // Arrange
            prepareInitializedChatMocks()
            val message = "Hello"

            // init chat
            viewModel.initChat(
                receiverId = receiverId,
                type = ChatViewModel.Companion.CHATTYPE.PRIVATE,
            )

            // procesamos lo pendiente
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // paramos polling para que no se enganche el test
            viewModel.stopMessagePolling()

            // procesamos lo pendiente
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // mock para enviar mensaje
            coEvery {
                sendMessageUseCase.get().invoke(receiverId, message)
            } returns MiraiLinkResult.Success(Unit)

            // Act
            viewModel.sendMessage(message)
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // Assert
            assert(viewModel.messages.value.any { it.content == message })
        }
}
