/**
 * @author Feryael Justice
 * @date 31/07/2024
 */
package com.feryaeljustice.mirailink.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.toMinimalUserInfo
import com.feryaeljustice.mirailink.data.mappers.ui.toChatMessageViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toMinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.di.IoDispatcher
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
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val createPrivateChatUseCase: Lazy<CreatePrivateChatUseCase>,
        private val createGroupChatUseCase: Lazy<CreateGroupChatUseCase>,
        private val getChatMessagesUseCase: Lazy<GetChatMessagesUseCase>,
        private val markChatAsReadUseCase: Lazy<MarkChatAsReadUseCase>,
        private val sendMessageUseCase: Lazy<SendMessageUseCase>,
        private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase>,
        private val getUserByIdUseCase: Lazy<GetUserByIdUseCase>,
        private val reportUseCase: Lazy<ReportUseCase>,
        private val logger: Logger,
        @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private val _chatId = MutableStateFlow<String?>(null)
        val chatId: StateFlow<String?> = _chatId

        private val _messages = MutableStateFlow<List<ChatMessageViewEntry>>(emptyList())
        val messages: StateFlow<List<ChatMessageViewEntry>> = _messages

        private val _sender = MutableStateFlow<MinimalUserInfoViewEntry?>(null)
        val sender: StateFlow<MinimalUserInfoViewEntry?> = _sender

        private val _receiver = MutableStateFlow<MinimalUserInfoViewEntry?>(null)
        val receiver: StateFlow<MinimalUserInfoViewEntry?> = _receiver

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
                    createPrivateChatUseCase.get()(receiverId)
                }

            if (result is MiraiLinkResult.Success) {
                _chatId.value = result.data
                proceedWithPrivateChatSetup(result.data, receiverId)
            } else if (result is MiraiLinkResult.Error) {
                logger.d("ChatViewModel", "initPrivateChat error: ${result.message}")
            }
        }

        private suspend fun initGroupChat(
            name: String? = null,
            userIds: List<String>? = null,
        ) {
            if (name == null || userIds == null) return

            val result =
                withContext(ioDispatcher) {
                    createGroupChatUseCase.get()(name, userIds)
                }

            if (result is MiraiLinkResult.Success) {
                // TODO: Implement group chat setup
                startGroupMessagesPolling("")
            } else if (result is MiraiLinkResult.Error) {
                logger.d("ChatViewModel", "initGroupChat error: ${result.message}")
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

                if (_sender.value != null && _receiver.value != null) {
                    startMessagePolling(receiverId)
                }
            }
        }

        fun markChatAsRead(chatId: String) {
            viewModelScope.launch {
                withContext(ioDispatcher) {
                    markChatAsReadUseCase.get()(chatId)
                }
            }
        }

        suspend fun setSenderSync() {
            val result =
                withContext(ioDispatcher) {
                    getCurrentUserUseCase.get()()
                }

            if (result is MiraiLinkResult.Success) {
                _sender.value = result.data.toMinimalUserInfo().toMinimalUserInfoViewEntry()
            } else if (result is MiraiLinkResult.Error) {
                logger.d("ChatViewModel", "getCurrentUserUseCase error: ${result.message}")
            }
        }

        suspend fun setReceiverSync(receiverId: String) {
            val result =
                withContext(ioDispatcher) {
                    getUserByIdUseCase.get()(receiverId)
                }

            if (result is MiraiLinkResult.Success) {
                _receiver.value = result.data.toMinimalUserInfo().toMinimalUserInfoViewEntry()
            } else if (result is MiraiLinkResult.Error) {
                logger.d("ChatViewModel", "setReceiver: ${result.message}")
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
                        getChatMessagesUseCase.get()(userId)
                    }

                if (result is MiraiLinkResult.Success) {
                    _messages.value = result.data.map { it.toChatMessageViewEntry() }
                } else if (result is MiraiLinkResult.Error) {
                    logger.d("ChatViewModel", "getMessages: ${result.message}")
                }
            }
        }

        fun sendMessage(content: String) {
            viewModelScope.launch {
                val currSender = _sender.value
                val currReceiver = _receiver.value

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
                        sendMessageUseCase.get()(newMessage.receiver.id, newMessage.content)
                    }

                if (result is MiraiLinkResult.Success) {
                    _messages.update { it.plus(newMessage) }
                    logger.d("ChatViewModel", "Message sent successfully")
                } else if (result is MiraiLinkResult.Error) {
                    logger.d("ChatViewModel", "sendMessage: ${result.message}")
                }
            }
        }

        fun reportUser(
            userId: String,
            reason: String,
    ) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                reportUseCase.get()(userId, reason)
            }.also { result ->
                if (result is MiraiLinkResult.Success) {
                    logger.d("ChatViewModel", "reportUser successfully")
                } else if (result is MiraiLinkResult.Error) {
                    logger.d("ChatViewModel", "reportUser error: ${result.message}")
                }
            }
        }
    }

    fun resetChatState() {
        _chatId.value = null
        _messages.value = emptyList()
        _sender.value = null
        _receiver.value = null
        stopMessagePolling()
    }
}
