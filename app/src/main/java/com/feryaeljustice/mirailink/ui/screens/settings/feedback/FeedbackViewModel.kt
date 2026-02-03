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

    val uiState: StateFlow<FeedbackState>
        field = MutableStateFlow<FeedbackState>(FeedbackState())

    fun updateFeedback(feedback: String) {
        uiState.update { it.copy(feedback = feedback) }
    }

    fun sendFeedback(onFinish: () -> Unit) {
        viewModelScope.launch {
            uiState.update { it.copy(loading = true, error = null) }

            val result =
                withContext(ioDispatcher) {
                    sendFeedbackUseCase(uiState.value.feedback)
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    uiState.update {
                        it.copy(loading = false, error = null, feedback = "")
                    }
                    onFinish()
                }

                is MiraiLinkResult.Error -> {
                    uiState.update {
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
