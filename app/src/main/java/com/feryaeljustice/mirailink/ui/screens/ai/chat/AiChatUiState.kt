package com.feryaeljustice.mirailink.ui.screens.ai.chat

sealed class AiChatUiState {
    data object Idle : AiChatUiState()

    data object Loading : AiChatUiState()

    data class Success(
        val response: String,
    ) : AiChatUiState()

    data class Error(
        val message: String,
    ) : AiChatUiState()
}
