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

    fun sendMessage(prompt: String) {
        if (prompt.isBlank() || uiState.value is AiChatUiState.Loading) return
        setRecoveryAction { sendMessage(prompt) }
        _messages.value = _messages.value + AiChatMessage(text = prompt.trim(), isUser = true)
        viewModelScope.launch {
            uiState.value = AiChatUiState.Loading
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
                    uiState.value = AiChatUiState.Error(result.error.toUiError())
                }
            }
        }
    }
}
