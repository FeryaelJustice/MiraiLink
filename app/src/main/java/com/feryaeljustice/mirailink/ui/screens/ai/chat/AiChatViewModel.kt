package com.feryaeljustice.mirailink.ui.screens.ai.chat

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun sendMessage(prompt: String) {
        setRecoveryAction { sendMessage(prompt) }
        viewModelScope.launch {
            uiState.value = AiChatUiState.Loading
            val result =
                withContext(ioDispatcher) {
                    generateContentUseCase(prompt = prompt)
                }
            when (result) {
                is MiraiLinkResult.Success -> {
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
