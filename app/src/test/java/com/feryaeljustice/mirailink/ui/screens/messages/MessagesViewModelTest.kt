// Author: Feryael Justice
// Date: 2025-11-08

package com.feryaeljustice.mirailink.ui.screens.messages

import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases
import com.feryaeljustice.mirailink.domain.usecase.chat.ConnectSocketUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.DisconnectSocketUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatsFromUser
import com.feryaeljustice.mirailink.domain.usecase.chat.ListenForMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase
import com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase
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
import java.util.Date

@ExperimentalCoroutinesApi
class MessagesViewModelTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: MessagesViewModel
    private val getMatchesUseCase: Lazy<GetMatchesUseCase> = mockk()
    private val chatUseCases: Lazy<ChatUseCases> = mockk()
    private val getChatsFromUser: GetChatsFromUser = mockk()

    private val user =
        User(
            "1",
            "user",
            "user",
            "user@test.com",
            null,
            null,
            null,
            null,
            emptyList(),
            emptyList(),
            emptyList(),
        )
    private val chatSummary =
        ChatSummary("1", ChatType.PRIVATE, "", Date(), Date(), ChatRole.MEMBER)

    @Before
    fun setUp() {
        val connectSocketUseCase = mockk<ConnectSocketUseCase>()
        val disconnectSocketUseCase = mockk<DisconnectSocketUseCase>()
        val createPrivateChatUseCase = mockk<CreatePrivateChatUseCase>()
        val createGroupChatUseCase = mockk<CreateGroupChatUseCase>()
        val sendMessageUseCase = mockk<SendMessageUseCase>()
        val listenForMessagesUseCase = mockk<ListenForMessagesUseCase>()

        val useCases =
            ChatUseCases(
                connect = connectSocketUseCase,
                disconnect = disconnectSocketUseCase,
                getChatsFromUser = getChatsFromUser,
                createPrivateChatUseCase = createPrivateChatUseCase,
                createGroupChatUseCase = createGroupChatUseCase,
                sendMessage = sendMessageUseCase,
                listenMessages = listenForMessagesUseCase,
            )

        coEvery { chatUseCases.get() } returns useCases
        coEvery { getChatsFromUser.invoke() } returns MiraiLinkResult.Success(listOf(chatSummary))
        coEvery { getMatchesUseCase.get().invoke() } returns MiraiLinkResult.Success(listOf(user))

        viewModel =
            MessagesViewModel(getMatchesUseCase, chatUseCases, mainCoroutineRule.testDispatcher)
        mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
    }

    @Test
    fun `load data success`() =
        runTest {
            viewModel.loadData()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()
            val state = viewModel.state.value
            assert(state is MessagesViewModel.MessagesUiState.Success)
            assert((state as MessagesViewModel.MessagesUiState.Success).matches.isNotEmpty())
            assert(state.openChats.isNotEmpty())
        }

    @Test
    fun `load matches error`() =
        runTest {
            coEvery { getMatchesUseCase.get().invoke() } returns MiraiLinkResult.Error("Error")

            viewModel.loadMatches()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is MessagesViewModel.MessagesUiState.Error)
        }

    @Test
    fun `load chats error`() =
        runTest {
            coEvery { getChatsFromUser.invoke() } returns MiraiLinkResult.Error("Error")

            viewModel.loadChats()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is MessagesViewModel.MessagesUiState.Error)
        }
}
