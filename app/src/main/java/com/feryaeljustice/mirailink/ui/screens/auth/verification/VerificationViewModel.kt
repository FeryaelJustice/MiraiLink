package com.feryaeljustice.mirailink.ui.screens.auth.verification

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class VerificationViewModel(
    private val checkIsVerifiedUseCase: CheckIsVerifiedUseCase,
    private val requestCodeUseCase: RequestVerificationCodeUseCase,
    private val confirmCodeUseCase: ConfirmVerificationCodeUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {
    data class VerificationState(
        val step: Int = 1,
        val token: String = "",
        val cooldownSeconds: Int = 0,
        val isLoading: Boolean = false,
        val error: UiError? = null,
    )

    val state: StateFlow<VerificationState>
        field = MutableStateFlow<VerificationState>(VerificationState())

    private var cooldownJob: Job? = null

    fun init(userId: String, initialToken: String = "") {
        state.update {
            it.copy(
                step = 2,
                token = if (initialToken.isNotBlank()) initialToken else it.token,
            )
        }
    }

    fun startCooldown(seconds: Int = 60) {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            for (remaining in seconds downTo 1) {
                state.update { it.copy(cooldownSeconds = remaining) }
                delay(1000)
            }
            state.update { it.copy(cooldownSeconds = 0) }
        }
    }

    fun onTokenChanged(token: String) {
        state.update { it.copy(token = token, error = null) }
    }

    fun checkUserIsVerified(onFinished: (isVerified: Boolean) -> Unit): Job = viewModelScope.launch {
        if (state.value.isLoading) return@launch
        state.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = withContext(ioDispatcher) { checkIsVerifiedUseCase() }) {
                is MiraiLinkResult.Success -> {
                    onFinished(result.data)
                    if (result.data) resetState()
                }
                is MiraiLinkResult.Error -> state.update {
                    it.copy(error = errorFor(result.error) { checkUserIsVerified(onFinished) })
                }
            }
        } finally {
            state.update { it.copy(isLoading = false) }
        }
    }

    fun requestCode(userId: String, onSent: () -> Unit = {}): Job = viewModelScope.launch {
        if (state.value.isLoading) return@launch
        state.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = withContext(ioDispatcher) { requestCodeUseCase(userId, "email") }) {
                is MiraiLinkResult.Success -> {
                    startCooldown(60)
                    state.update { it.copy(step = 2, error = null) }
                    onSent()
                }
                is MiraiLinkResult.Error -> state.update {
                    it.copy(error = errorFor(result.error) { requestCode(userId, onSent) })
                }
            }
        } finally {
            state.update { it.copy(isLoading = false) }
        }
    }

    fun confirmCode(userId: String, onFinish: () -> Unit): Job = viewModelScope.launch {
        if (state.value.isLoading) return@launch
        val code = state.value.token
        state.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = withContext(ioDispatcher) { confirmCodeUseCase(userId, code, "email") }) {
                is MiraiLinkResult.Success -> {
                    resetState()
                    onFinish()
                }
                is MiraiLinkResult.Error -> state.update {
                    it.copy(error = errorFor(result.error) { confirmCode(userId, onFinish) })
                }
            }
        } finally {
            state.update { it.copy(isLoading = false) }
        }
    }

    private fun resetState() {
        cooldownJob?.cancel()
        state.value = VerificationState()
    }
}
