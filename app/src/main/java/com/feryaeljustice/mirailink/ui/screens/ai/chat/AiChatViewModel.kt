package com.feryaeljustice.mirailink.ui.screens.ai.chat

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class AiChatViewModel(
    private val generateContentUseCase: GenerateContentUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    val uiState: StateFlow<AiChatUiState>
        field = MutableStateFlow<AiChatUiState>(AiChatUiState.Idle)

    private val _messages = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val messages: StateFlow<List<AiChatMessage>> = _messages.asStateFlow()

    /**
     * Añade el texto visible antes de pedir una respuesta a Firebase AI y conserva el prompt
     * como accion de reintento. Reintentar solicita la respuesta sin duplicar el mensaje visible.
     * La lista vive en este ViewModel y no se envía como historial al datasource.
     */
    fun sendMessage(prompt: String) {
        if (prompt.isBlank() || uiState.value is AiChatUiState.Loading) return
        setRecoveryAction { sendMessage(prompt) }
        _messages.value = _messages.value + AiChatMessage(text = prompt.trim(), isUser = true)
        requestReply(prompt)
    }

    private fun requestReply(prompt: String) {
        if (uiState.value is AiChatUiState.Loading) return
        uiState.value = AiChatUiState.Loading
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    generateContentUseCase(prompt = prompt)
                }
            when (result) {
                is MiraiLinkResult.Success -> {
                    _messages.value = _messages.value + AiChatMessage(text = result.data, isUser = false)
                    uiState.value =
                        AiChatUiState.Success(response = result.data)
                }

                is MiraiLinkResult.Error -> {
                    uiState.value = AiChatUiState.Error(errorFor(result.error) { requestReply(prompt) })
                }
            }
        }
    }
}
