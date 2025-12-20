package com.feryaeljustice.mirailink.ui.screens.messages

import com.feryaeljustice.mirailink.domain.enums.ChatRole
import com.feryaeljustice.mirailink.domain.enums.ChatType
import com.feryaeljustice.mirailink.domain.model.chat.ChatSummary
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases
import com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.util.Date

@ExperimentalCoroutinesApi
class MessagesViewModelTest : KoinTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getMatchesUseCase: GetMatchesUseCase by inject()
    private val chatUseCases: ChatUseCases by inject()

    private lateinit var viewModel: MessagesViewModel

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<GetMatchesUseCase>() }
                    single {
                        mockk<ChatUseCases> {
                            coEvery { getChatsFromUser.invoke() } returns
                                MiraiLinkResult.Success(
                                    listOf(
                                        ChatSummary(
                                            "1",
                                            ChatType.PRIVATE,
                                            "",
                                            Date(),
                                            Date(),
                                            ChatRole.MEMBER,
                                        ),
                                    ),
                                )
                        }
                    }
                },
            )
        }

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

    @Before
    fun setUp() {
        coEvery { getMatchesUseCase.invoke() } returns MiraiLinkResult.Success(listOf(user))
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
            coEvery { getMatchesUseCase.invoke() } returns MiraiLinkResult.Error("Error")

            viewModel.loadMatches()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is MessagesViewModel.MessagesUiState.Error)
        }

    @Test
    fun `load chats error`() =
        runTest {
            coEvery { chatUseCases.getChatsFromUser.invoke() } returns MiraiLinkResult.Error("Error")

            viewModel.loadChats()
            mainCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.state.value
            assert(state is MessagesViewModel.MessagesUiState.Error)
        }
}
