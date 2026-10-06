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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString

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
    private val capsules: com.feryaeljustice.mirailink.domain.usecase.capsule.CapsuleUseCases? = null,
    private val savedState: androidx.lifecycle.SavedStateHandle = androidx.lifecycle.SavedStateHandle(),
    private val analytics: com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker? = null,
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

    val capsule: StateFlow<com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule?>
        field = MutableStateFlow<com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule?>(null)
    private val _unlockEffects = kotlinx.coroutines.flow.MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val unlockEffects = _unlockEffects.asSharedFlow()
    val capsuleBusy: StateFlow<Boolean>
        field = MutableStateFlow(false)
    val pendingWork: StateFlow<Boolean>
        field = MutableStateFlow(false)
    val messageBusy: StateFlow<Boolean>
        field = MutableStateFlow(false)
    private var ownerPeer: String? = null
    fun retryPending() { performErrorAction() }
    private val fetchMutex = kotlinx.coroutines.sync.Mutex()
    private val actionMutex = kotlinx.coroutines.sync.Mutex()
    private var screenActive = false
    private var baselineLoaded = false
    private val capsuleJson = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
    fun setScreenActive(active: Boolean) { screenActive = active; baselineLoaded = false }
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
            // TODO: Implementar la configuración del chat grupal
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
            val identity = sender.value?.id + ":" + receiverId
            if(savedState.get<String>("pendingOwner") != identity) {
                savedState.remove<String>("pendingText"); savedState.remove<String>("pendingId"); savedState.remove<String>("capsuleAction")
            }
            ownerPeer = identity
            pendingWork.value = savedState.get<String>("pendingText") != null || savedState.get<String>("capsuleAction") != null

            if (sender.value != null && receiver.value != null) {
                if (screenActive || capsules == null) startMessagePolling(receiverId)
                else fetchMessages(receiverId)
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
            capsule.value = capsules?.cached(receiverId)
        } else if (result is MiraiLinkResult.Error) {
            showError(result.error) { initChat(receiverId = receiverId, type = CHATTYPE.PRIVATE) }
        }
    }

    /**
     * Espera cada consulta antes del siguiente intervalo y comparte el mutex con las recargas manuales.
     */
    fun startMessagePolling(userId: String) {
        if (pollingJob?.isActive == true) return

        pollingJob =
            viewModelScope.launch {
                while (true) {
                    fetchMessages(userId)
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

    fun getMessages(userId: String) { viewModelScope.launch { fetchMessages(userId) } }

    private suspend fun fetchMessages(userId: String) = fetchMutex.withLock {
        if(capsules != null) {
            when(val result = withContext(ioDispatcher) { capsules.history(userId) }) {
                is MiraiLinkResult.Success -> {
                    messages.value = result.data.messages.map { it.toChatMessageViewEntry() }
                    val previous = capsule.value
                    val next = result.data.capsule
                    capsule.value = next
                    receiver.value = receiver.value?.copy(photoPresentation = next?.photoPresentation() ?: receiver.value?.photoPresentation)
                    if(screenActive && baselineLoaded && previous != null && next != null && next.level > previous.level) {
                        _unlockEffects.tryEmit(next.level)
                        analytics?.logEvent("capsule_unlock", mapOf("level" to next.level, "completed" to (next.status == "revealed")))
                    }
                    if(!baselineLoaded) {
                        val pendingAction = savedState.get<String>("capsuleAction")
                        val pendingText = savedState.get<String>("pendingText")
                        if(pendingAction != null && next != null) setRecoveryAction { executeCapsuleAction(next.id, capsuleJson.decodeFromString(pendingAction)) }
                        else if(pendingText != null) setRecoveryAction { sendMessage(pendingText) }
                    }
                    baselineLoaded = true
                    error.value = null
                }
                is MiraiLinkResult.Error -> showError(result.error) { getMessages(userId) }
            }
        } else {
            when(val result = withContext(ioDispatcher) { getChatMessagesUseCase(userId) }) {
                is MiraiLinkResult.Success -> { messages.value = result.data.map { it.toChatMessageViewEntry() }; error.value = null }
                is MiraiLinkResult.Error -> showError(result.error) { getMessages(userId) }
            }
        }
    }

    fun capsuleAction(type: String, category: String? = null, text: String? = null) {
        val current = capsule.value ?: return
        val action = com.feryaeljustice.mirailink.domain.model.capsule.CapsuleAction(UUID.randomUUID().toString(), current.revision,
            type, category, current.question?.instanceId, text)
        if(capsuleBusy.value || pendingWork.value) return
        savedState["pendingOwner"] = ownerPeer
        savedState["capsuleAction"] = capsuleJson.encodeToString(action)
        pendingWork.value = true
        executeCapsuleAction(current.id, action)
    }

    private fun executeCapsuleAction(id: String, action: com.feryaeljustice.mirailink.domain.model.capsule.CapsuleAction) {
        viewModelScope.launch { actionMutex.withLock {
            val useCases = capsules ?: return@withLock
            capsuleBusy.value = true
            try {
                when(val result = withContext(ioDispatcher) { useCases.act(id, action) }) {
                    is MiraiLinkResult.Success -> {
                        savedState.remove<String>("capsuleAction")
                        pendingWork.value = false
                        analytics?.logEvent("capsule_action", mapOf("action" to action.type, "category" to action.category,
                            "level" to result.data.level, "status" to result.data.status))
                        val previous = capsule.value
                        capsule.value = result.data
                        receiver.value = receiver.value?.copy(photoPresentation = result.data.photoPresentation())
                        if(screenActive && baselineLoaded && previous != null && result.data.level > previous.level) _unlockEffects.tryEmit(result.data.level)
                        // Refresh canonical messages and state together. This also emits a single foreground unlock.
                        error.value = null
                        receiver.value?.id?.let { fetchMessages(it) }
                    }
                    is MiraiLinkResult.Error -> {
                        receiver.value?.id?.let { fetchMessages(it) }
                        if(result.error is com.feryaeljustice.mirailink.domain.error.CapsuleError) {
                            savedState.remove<String>("capsuleAction"); pendingWork.value = false
                            showError(result.error) { receiver.value?.id?.let { getMessages(it) } }
                        } else showError(result.error) { executeCapsuleAction(id, action) }
                    }
                }
            } finally { capsuleBusy.value = false }
        } }
    }

    /**
     * Conserva el identificador del envío pendiente y recarga los mensajes confirmados después del éxito.
     */
    fun sendMessage(content: String) {
        if (capsules != null) {
            val peer = receiver.value?.id ?: return
            val pendingText: String? = savedState["pendingText"]
            val clientId = if(pendingText == content) savedState.get<String>("pendingId") ?: UUID.randomUUID().toString() else UUID.randomUUID().toString()
            if(messageBusy.value || (pendingWork.value && pendingText != content)) return
            messageBusy.value = true
            pendingWork.value = true
            savedState["pendingOwner"] = ownerPeer
            savedState["pendingText"] = content
            savedState["pendingId"] = clientId
            viewModelScope.launch { actionMutex.withLock {
                try {
                when(val result = withContext(ioDispatcher) { capsules.send(peer, content, clientId) }) {
                    is MiraiLinkResult.Success -> { pendingWork.value = false; savedState.remove<String>("pendingText"); savedState.remove<String>("pendingId"); fetchMessages(peer) }
                    is MiraiLinkResult.Error -> showError(result.error) { sendMessage(content) }
                }
                } finally { messageBusy.value = false }
            } }
            return
        }
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

    fun sendGestureChallengeInvite() {
        sendMessage(com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessagePayload.formatInvite())
    }

    fun sendGestureChallengeResult(summary: com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureChallengeSummary) {
        sendMessage(com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessagePayload.formatResult(summary))
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
        capsule.value = null
        baselineLoaded = false
        chatId.value = null
        messages.value = emptyList()
        sender.value = null
        receiver.value = null
        error.value = null
        stopMessagePolling()
    }
}
