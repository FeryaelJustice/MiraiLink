package com.feryaeljustice.mirailink.ui.screens.ai.chat

import com.feryaeljustice.mirailink.ui.error.UiError

sealed class AiChatUiState {
    data object Idle : AiChatUiState()

    data object Loading : AiChatUiState()

    data class Success(
        val response: String,
    ) : AiChatUiState()

    data class Error(
        val error: UiError,
    ) : AiChatUiState()
}
