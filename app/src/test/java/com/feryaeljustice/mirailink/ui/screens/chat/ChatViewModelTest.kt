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
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ChatViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Injected Mocks
    private val createPrivateChatUseCase: CreatePrivateChatUseCase by inject()
    private val createGroupChatUseCase: CreateGroupChatUseCase by inject()
    private val getChatMessagesUseCase: GetChatMessagesUseCase by inject()
    private val markChatAsReadUseCase: MarkChatAsReadUseCase by inject()
    private val sendMessageUseCase: SendMessageUseCase by inject()
    private val getCurrentUserUseCase: GetCurrentUserUseCase by inject()
    private val getUserByIdUseCase: GetUserByIdUseCase by inject()
    private val reportUseCase: ReportUseCase by inject()
    private val logger: Logger by inject()

    // ViewModel
    private lateinit var viewModel: ChatViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<CreatePrivateChatUseCase>() }
                    single { mockk<CreateGroupChatUseCase>() }
                    single { mockk<GetChatMessagesUseCase>() }
                    single { mockk<MarkChatAsReadUseCase>() }
                    single { mockk<SendMessageUseCase>() }
                    single { mockk<GetCurrentUserUseCase>() }
                    single { mockk<GetUserByIdUseCase>() }
                    single { mockk<ReportUseCase>() }
                    single { mockk<Logger>(relaxed = true) }
                },
            )
        }

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
            createPrivateChatUseCase.invoke(receiverId)
        } returns MiraiLinkResult.Success(chatId)
        coEvery { getCurrentUserUseCase.invoke() } returns MiraiLinkResult.Success(sender)
        coEvery { getUserByIdUseCase.invoke(receiverId) } returns
            MiraiLinkResult.Success(
                receiver,
            )
        coEvery { markChatAsReadUseCase.invoke(chatId) } returns MiraiLinkResult.Success(Unit)
        // da igual lo que devuelva, pero que no falle
        coEvery { getChatMessagesUseCase.invoke(any()) } returns
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
                sendMessageUseCase.invoke(receiverId, message)
            } returns MiraiLinkResult.Success(Unit)

            // Act
            viewModel.sendMessage(message)
            mainCoroutineRule.testDispatcher.scheduler.runCurrent()

            // Assert
            assert(viewModel.messages.value.any { it.content == message })
        }
}
