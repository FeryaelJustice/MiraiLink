package com.feryaeljustice.mirailink.ui.screens.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.mappers.toMinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.usecase.chat.CreateGroupChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.CreatePrivateChatUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.GetChatMessagesUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.MarkChatAsReadUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.SendMessageUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetUserByIdUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val createPrivateChatUseCase: CreatePrivateChatUseCase,
    private val createGroupChatUseCase: CreateGroupChatUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val markChatAsReadUseCase: MarkChatAsReadUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : ViewModel() {

    private val _chatId = MutableStateFlow<String?>(null)
    val chatId: StateFlow<String?> = _chatId

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _sender = MutableStateFlow<MinimalUserInfo?>(null)
    val sender: StateFlow<MinimalUserInfo?> = _sender
    private val _isSenderReady = MutableStateFlow(false)

    private val _receiver = MutableStateFlow<MinimalUserInfo?>(null)
    val receiver: StateFlow<MinimalUserInfo?> = _receiver
    private val _isReceiverReady = MutableStateFlow(false)

    private var pollingJob: Job? = null

    companion object {
        enum class CHATTYPE {
            PRIVATE,
            GROUP
        }
    }

    fun initChat(
        receiverId: String? = null,
        name: String? = "",
        userIds: List<String>? = null,
        type: CHATTYPE
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
        when (val result = createPrivateChatUseCase(receiverId)) {
            is MiraiLinkResult.Success -> {
                _chatId.value = result.data
                proceedWithPrivateChatSetup(result.data, receiverId)
            }

            is MiraiLinkResult.Error -> {
                Log.e("ChatViewModel", "initPrivateChat error: ${result.message}")
            }
        }
    }

    private fun proceedWithPrivateChatSetup(chatId: String, receiverId: String) {
        markChatAsRead(chatId)
        setReceiver(receiverId)
        setSender()
        startPolling(receiverId)
    }

    private suspend fun initGroupChat(name: String? = null, userIds: List<String>? = null) {
        if (name == null || userIds == null) return
        when (val result = createGroupChatUseCase(name, userIds)) {
            is MiraiLinkResult.Success -> {
                //TODO
                //setSender()
                startGroupMessagesPolling("")
            }

            is MiraiLinkResult.Error -> {
                Log.e("ChatViewModel", "initGroupChat error: ${result.message}")
            }
        }
    }

    fun markChatAsRead(chatId: String) {
        viewModelScope.launch {
            markChatAsReadUseCase(chatId)
        }
    }

    fun setSender() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    val user = result.data
                    _sender.value = user.toMinimalUserInfo()
                    _isSenderReady.value = true

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
                    _isReceiverReady.value = true
                }

                is MiraiLinkResult.Error -> {
                    Log.e("ChatViewModel", "setReceiver: ${res.message}")
                }
            }
        }
    }

    private fun startPolling(receiverId: String){
        viewModelScope.launch {
            _isReceiverReady.collect { ready ->
                if (ready) {
                    startMessagePolling(receiverId)
                    _isReceiverReady.value = false
                    _isSenderReady.value = false
                    cancel()
                }
            }
        }
    }

    fun startMessagePolling(userId: String) {
        if (pollingJob?.isActive == true) return
        pollingJob = viewModelScope.launch {
            while (true) {
                getMessages(userId)
                delay(3000L) // cada 3 segundos
            }
        }
    }

    fun startGroupMessagesPolling(chatId: String) {
        Log.d("ChatViewModel", "startGroupMessagesPolling: $chatId")
        //TODO
    }

    fun stopMessagePolling() {
        pollingJob?.cancel()
        pollingJob = null
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

    fun resetChatState() {
        _chatId.value = null
        _messages.value = emptyList()
        _sender.value = null
        _receiver.value = null
        _isSenderReady.value = false
        _isReceiverReady.value = false
        stopMessagePolling()
    }

}