/**
 * @author Feryael Justice
 * @date 31/07/2024
 */
package com.feryaeljustice.mirailink.ui.screens.chat

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.toMinimalUserInfo
import com.feryaeljustice.mirailink.data.mappers.ui.toChatMessageViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toMinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.domain.error.AppError
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
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel
import java.util.UUID

@KoinViewModel
class ChatViewModel(
    private val createPrivateChatUseCase: CreatePrivateChatUseCase,
    private val createGroupChatUseCase: CreateGroupChatUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val markChatAsReadUseCase: MarkChatAsReadUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val reportUseCase: ReportUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    val chatId: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    val messages: StateFlow<List<ChatMessageViewEntry>>
        field = MutableStateFlow<List<ChatMessageViewEntry>>(emptyList())

    val sender: StateFlow<MinimalUserInfoViewEntry?>
        field = MutableStateFlow<MinimalUserInfoViewEntry?>(null)

    val receiver: StateFlow<MinimalUserInfoViewEntry?>
        field = MutableStateFlow<MinimalUserInfoViewEntry?>(null)

    val error: StateFlow<UiError?>
        field = MutableStateFlow<UiError?>(null)

    private var pollingJob: Job? = null

    companion object {
        enum class CHATTYPE {
            PRIVATE,
            GROUP,
        }
    }

    fun initChat(
        receiverId: String? = null,
        name: String? = "",
        userIds: List<String>? = null,
        type: CHATTYPE,
    ) {
        error.value = null
        setRecoveryAction { initChat(receiverId, name, userIds, type) }
        // Tanto el init private y group chat, sus usecases devuelven el chatId
        viewModelScope.launch {
            when (type) {
                CHATTYPE.PRIVATE -> {
                    initPrivateChat(receiverId)
                }

                CHATTYPE.GROUP -> {
                    initGroupChat(name, userIds)
                }
            }
        }
    }

    private suspend fun initPrivateChat(receiverId: String? = null) {
        if (receiverId == null) return

        val result =
            withContext(ioDispatcher) {
                createPrivateChatUseCase(receiverId)
            }

        if (result is MiraiLinkResult.Success) {
            chatId.value = result.data
            proceedWithPrivateChatSetup(result.data, receiverId)
        } else if (result is MiraiLinkResult.Error) {
            showError(result.error) { initChat(receiverId = receiverId, type = CHATTYPE.PRIVATE) }
        }
    }

    private suspend fun initGroupChat(
        name: String? = null,
        userIds: List<String>? = null,
    ) {
        if (name == null || userIds == null) return

        val result =
            withContext(ioDispatcher) {
                createGroupChatUseCase(name, userIds)
            }

        if (result is MiraiLinkResult.Success) {
            // TODO: Implement group chat setup
            startGroupMessagesPolling("")
        } else if (result is MiraiLinkResult.Error) {
            showError(result.error) { initChat(name = name, userIds = userIds, type = CHATTYPE.GROUP) }
        }
    }

    private fun proceedWithPrivateChatSetup(
        chatId: String,
        receiverId: String,
    ) {
        viewModelScope.launch {
            markChatAsRead(chatId)

            // Esperamos a que ambas funciones terminen antes de avanzar
            setReceiverSync(receiverId)
            setSenderSync()

            if (sender.value != null && receiver.value != null) {
                startMessagePolling(receiverId)
            }
        }
    }

    fun markChatAsRead(chatId: String) {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { markChatAsReadUseCase(chatId) }) {
                is MiraiLinkResult.Success -> error.value = null
                is MiraiLinkResult.Error -> showError(result.error) { markChatAsRead(chatId) }
            }
        }
    }

    suspend fun setSenderSync() {
        val result =
            withContext(ioDispatcher) {
                getCurrentUserUseCase()
            }

        if (result is MiraiLinkResult.Success) {
            sender.value = result.data.toMinimalUserInfo().toMinimalUserInfoViewEntry()
        } else if (result is MiraiLinkResult.Error) {
            error.value = result.error.toUiError()
        }
    }

    suspend fun setReceiverSync(receiverId: String) {
        val result =
            withContext(ioDispatcher) {
                getUserByIdUseCase(receiverId)
            }

        if (result is MiraiLinkResult.Success) {
            receiver.value = result.data.toMinimalUserInfo().toMinimalUserInfoViewEntry()
        } else if (result is MiraiLinkResult.Error) {
            showError(result.error) { initChat(receiverId = receiverId, type = CHATTYPE.PRIVATE) }
        }
    }

    fun startMessagePolling(userId: String) {
        if (pollingJob?.isActive == true) return

        pollingJob =
            viewModelScope.launch {
                while (true) {
                    getMessages(userId)
                    delay(3000L) // cada 3 segundos
                }
            }
    }

    fun startGroupMessagesPolling(chatId: String) {
        logger.d("ChatViewModel", "startGroupMessagesPolling: $chatId")
        // TODO
    }

    fun stopMessagePolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun getMessages(userId: String) {
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    getChatMessagesUseCase(userId)
                }

            if (result is MiraiLinkResult.Success) {
                messages.value = result.data.map { it.toChatMessageViewEntry() }
                error.value = null
            } else if (result is MiraiLinkResult.Error) {
                showError(result.error) { getMessages(userId) }
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            val currSender = sender.value
            val currReceiver = receiver.value

            if (currSender == null || currReceiver == null) return@launch

            val newMessage =
                ChatMessageViewEntry(
                    id = UUID.randomUUID().toString(),
                    sender = currSender,
                    receiver = currReceiver,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                )

            val result =
                withContext(ioDispatcher) {
                    sendMessageUseCase(newMessage.receiver.id, newMessage.content)
                }

            if (result is MiraiLinkResult.Success) {
                messages.update { it.plus(newMessage) }
                error.value = null
                logger.d("ChatViewModel", "Message sent successfully")
            } else if (result is MiraiLinkResult.Error) {
                showError(result.error) { sendMessage(content) }
            }
        }
    }

    fun reportUser(
        userId: String,
        reason: String,
    ) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                reportUseCase(userId, reason)
            }.also { result ->
                if (result is MiraiLinkResult.Success) {
                    error.value = null
                    logger.d("ChatViewModel", "reportUser successfully")
                } else if (result is MiraiLinkResult.Error) {
                    showError(result.error) { reportUser(userId, reason) }
                }
            }
        }
    }

    private fun showError(appError: AppError, recovery: () -> Unit) {
        setRecoveryAction(recovery)
        error.value = appError.toUiError()
    }

    fun resetChatState() {
        chatId.value = null
        messages.value = emptyList()
        sender.value = null
        receiver.value = null
        error.value = null
        stopMessagePolling()
    }
}
