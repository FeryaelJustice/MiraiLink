package com.feryaeljustice.mirailink.ui.screens.ai.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.ai.GenerateContentUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AiChatViewModel(
    private val generateContentUseCase: GenerateContentUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AiChatUiState>(AiChatUiState.Idle)
    val uiState: StateFlow<AiChatUiState> = _uiState

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _uiState.value = AiChatUiState.Loading
            val result =
                withContext(ioDispatcher) {
                    generateContentUseCase(prompt = prompt)
                }
            when (result) {
                is MiraiLinkResult.Success -> {
                    _uiState.value =
                        AiChatUiState.Success(response = result.data)
                }

                is MiraiLinkResult.Error -> {
                    _uiState.value = AiChatUiState.Error(result.message)
                }
            }
        }
    }
}
