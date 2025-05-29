package com.feryaeljustice.mirailink.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.toMinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.ChatMessage
import com.feryaeljustice.mirailink.domain.model.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _sender = MutableStateFlow<MinimalUserInfo?>(null)
    val sender: StateFlow<MinimalUserInfo?> = _sender

    private val _receiver = MutableStateFlow<MinimalUserInfo?>(null)
    val receiver: StateFlow<MinimalUserInfo?> = _receiver

    init {
        setSender()
    }

    fun setSender() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    val user = result.data
                    _sender.value = user.toMinimalUserInfo()

                    Log.d(
                        "ChatViewModel getCurrentUserUseCase",
                        "getCurrentUserUseCase successfully"
                    )
                }

                is MiraiLinkResult.Error -> {
                    Log.e(
                        "ChatViewModel getCurrentUserUseCase",
                        "getCurrentUserUseCase error: ${result.message}"
                    )
                }
            }
        }
    }

    fun setReceiver(receiverId: String) {
        viewModelScope.launch {
            when (val res = getUserByIdUseCase(receiverId)) {
                is MiraiLinkResult.Success -> {
                    val user = res.data
                    _receiver.value = user.toMinimalUserInfo()
                    _receiver.value?.let { rec ->
                        getMessages(rec.id)
                    }
                }

                is MiraiLinkResult.Error -> {
                    Log.e("ChatViewModel", "setReceiver: ${res.message}")
                }
            }
        }
    }

    fun getMessages(userId: String) {
        viewModelScope.launch {
            when (val result = getChatMessagesUseCase(userId)) {
                is MiraiLinkResult.Success -> {
                    val msgList = result.data
                    _messages.value = msgList
                    Log.d("ChatViewModel", "Messages retrieved successfully")
                }

                is MiraiLinkResult.Error -> {
                    Log.e(
                        "ChatViewModel",
                        "getMessages: ${result.message} ${result.exception?.message}"
                    )
                }
            }
        }
    }

    fun sendMessage(content: String) {
        viewModelScope.launch {
            val currSender = _sender.value
            val currReceiver = _receiver.value

            if (currSender == null || currReceiver == null) return@launch

            val newMessage = ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = currSender,
                receiver = currReceiver,
                content = content,
                timestamp = System.currentTimeMillis()
            )

            when (val result = sendMessageUseCase(newMessage.receiver.id, newMessage.content)) {
                is MiraiLinkResult.Success -> {
                    Log.d("ChatViewModel", "Message sent successfully")
                    _messages.update { it.plus(newMessage) }
                }

                is MiraiLinkResult.Error -> {
                    Log.e("ChatViewModel", "sendMessage: ${result.message}")
                }
            }
        }
    }
}