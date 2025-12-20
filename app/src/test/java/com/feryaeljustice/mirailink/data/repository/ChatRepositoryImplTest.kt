package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.core.UnitTest
import com.feryaeljustice.mirailink.data.datasource.ChatRemoteDataSource
import com.feryaeljustice.mirailink.data.model.UserDto
import com.feryaeljustice.mirailink.data.model.response.chat.ChatMessageResponse
import com.feryaeljustice.mirailink.data.model.response.chat.ChatSummaryResponse
import com.feryaeljustice.mirailink.data.model.response.user.MinimalUserInfoResponse
import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.di.koin.Qualifiers
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class ChatRepositoryImplTest : UnitTest() {
    private val chatRepository: ChatRepositoryImpl by inject()
    private val remoteDataSource: ChatRemoteDataSource by inject()
    private val socketService: SocketService by inject()

    @get:Rule
    val koinTestRule =
        KoinTestRule.create {
            modules(
                module {
                    single { mockk<ChatRemoteDataSource>() }
                    single { mockk<SocketService>(relaxed = true) }
                    single(Qualifiers.BaseUrl) { "http://localhost:8080" }
                    single { ChatRepositoryImpl(get(), get(), get(Qualifiers.BaseUrl)) }
                },
            )
        }

    private val userDto = UserDto(id = "user1", username = "testuser", nickname = "Test User")

    private val chatSummaryResponse =
        ChatSummaryResponse(
            id = "chat1",
            type = "private",
            createdBy = "user1",
            createdAt = "2024-01-01T12:00:00.000Z",
            joinedAt = "2024-01-01T12:00:00.000Z",
            role = "member",
            unreadCount = "0",
            destinatary =
                MinimalUserInfoResponse(
                    id = "user2",
                    username = "otheruser",
                    nickname = "Other User",
                    avatarUrl = null,
                ),
        )

    private val chatMessageResponse =
        ChatMessageResponse(
            id = "msg1",
            sender = userDto,
            receiver = userDto,
            content = "Hello",
            timestamp = 1672531200000,
        )

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `getChatsFromUser returns success when remote source is successful`() =
        runTest {
            // Given
            val responseList = listOf(chatSummaryResponse)
            coEvery { remoteDataSource.getChatsFromUser() } returns MiraiLinkResult.Success(responseList)

            // When
            val result = chatRepository.getChatsFromUser()

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val chats = (result as MiraiLinkResult.Success).data
            assertThat(chats).hasSize(1)
            assertThat(chats.first().id).isEqualTo(chatSummaryResponse.id)
        }

    @Test
    fun `getMessagesWith returns success when remote source is successful`() =
        runTest {
            // Given
            val userId = "user2"
            val responseList = listOf(chatMessageResponse)
            coEvery { remoteDataSource.getChatHistory(userId) } returns MiraiLinkResult.Success(responseList)

            // When
            val result = chatRepository.getMessagesWith(userId)

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            val messages = (result as MiraiLinkResult.Success).data
            assertThat(messages).hasSize(1)
            assertThat(messages.first().id).isEqualTo(chatMessageResponse.id)
        }

    @Test
    fun `markChatAsRead returns success`() =
        runTest {
            // Given
            val chatId = "chat1"
            coEvery { remoteDataSource.markChatAsRead(chatId) } returns MiraiLinkResult.Success(Unit)

            // When
            val result = chatRepository.markChatAsRead(chatId)

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        }

    @Test
    fun `createPrivateChat returns success`() =
        runTest {
            // Given
            val otherUserId = "user2"
            val chatId = "newChatId"
            coEvery { remoteDataSource.createPrivateChat(otherUserId) } returns MiraiLinkResult.Success(chatId)

            // When
            val result = chatRepository.createPrivateChat(otherUserId)

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
            assertThat((result as MiraiLinkResult.Success).data).isEqualTo(chatId)
        }

    @Test
    fun `sendMessageTo returns success`() =
        runTest {
            // Given
            val userId = "user2"
            val content = "Hello there!"
            coEvery { remoteDataSource.sendMessage(userId, content) } returns MiraiLinkResult.Success(Unit)

            // When
            val result = chatRepository.sendMessageTo(userId, content)

            // Then
            assertThat(result).isInstanceOf(MiraiLinkResult.Success::class.java)
        }
}
