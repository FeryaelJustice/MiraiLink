package com.feryaeljustice.mirailink.ui.screens.settings.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.feedback.SendFeedbackUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class FeedbackViewModel(
    private val sendFeedbackUseCase: SendFeedbackUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedbackState())
    val uiState: StateFlow<FeedbackState> = _uiState

    fun updateFeedback(feedback: String) {
        _uiState.update { it.copy(feedback = feedback) }
    }

    fun sendFeedback(onFinish: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }

            val result =
                withContext(ioDispatcher) {
                    sendFeedbackUseCase(_uiState.value.feedback)
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    _uiState.update {
                        it.copy(loading = false, error = null, feedback = "")
                    }
                    onFinish()
                }

                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(loading = false, error = result.message)
                    }
                }
            }
        }
    }
}

data class FeedbackState(
    val loading: Boolean = false,
    val error: String? = null,
    val feedback: String = "",
)
