package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class RecoverPasswordViewModel(
    private val requestResetUseCase: RequestPasswordResetUseCase,
    private val confirmResetUseCase: ConfirmPasswordResetUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {
    data class PasswordResetState(
        val step: Int = 1,
        val email: String = "",
        val token: String = "",
        val newPassword: String = "",
        val confirmPassword: String = "",
        val isResetSuccess: Boolean = false,
        val isLoading: Boolean = false,
        val error: UiError? = null,
    ) {
        val passwordsMatch: Boolean
            get() = confirmPassword.isEmpty() || newPassword == confirmPassword
    }

    val state: StateFlow<PasswordResetState>
    field = MutableStateFlow<PasswordResetState>(PasswordResetState())

    fun initEmail(initialEmail: String) = state.update {
        if (it.email.isBlank()) it.copy(email = initialEmail) else it
    }

    fun initToken(token: String, initialEmail: String? = null) {
        state.update {
            it.copy(
                token = token.ifBlank { it.token },
                newPassword = if (token != it.token) "" else it.newPassword,
                confirmPassword = if (token != it.token) "" else it.confirmPassword,
                email = initialEmail?.takeIf { e -> e.isNotBlank() } ?: it.email,
                step = if (token.isNotBlank()) 2 else it.step,
            )
        }
    }

    fun onEmailChanged(email: String) {
        state.update { it.copy(email = email, error = null) }
    }

    fun onTokenChanged(token: String) {
        state.update { it.copy(token = token, error = null) }
    }

    fun onPasswordChanged(password: String) {
        state.update { it.copy(newPassword = password, error = null) }
    }

    fun onConfirmPasswordChanged(password: String) {
        state.update { it.copy(confirmPassword = password, error = null) }
    }

    fun requestReset(): Job = viewModelScope.launch {
        if (state.value.isLoading) return@launch
        val email = state.value.email
        state.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = withContext(ioDispatcher) { requestResetUseCase(email) }) {
                is MiraiLinkResult.Success -> state.update { it.copy(step = 2, error = null) }
                is MiraiLinkResult.Error -> state.update { it.copy(error = errorFor(result.error) { requestReset() }) }
            }
        } finally {
            state.update { it.copy(isLoading = false) }
        }
    }

    fun confirmReset(onConfirmed: () -> Unit = {}): Job = viewModelScope.launch {
        if (state.value.isLoading) return@launch
        val input = state.value
        if (input.confirmPassword.isNotEmpty() && input.newPassword != input.confirmPassword) {
            val error = rememberError(
                UiError(
                    message = UiText.Resource(R.string.error_passwords_do_not_match),
                    actionLabel = UiText.Resource(R.string.action_review),
                    recovery = ErrorRecovery.REVIEW_INPUT,
                ),
            ) { state.update { it.copy(error = null) } }
            state.update { it.copy(error = error) }
            return@launch
        }
        state.update { it.copy(isLoading = true, error = null) }
        try {
            when (val result = withContext(ioDispatcher) { confirmResetUseCase(input.email, input.token, input.newPassword) }) {
                is MiraiLinkResult.Success -> {
                    state.update { it.copy(isResetSuccess = true, error = null) }
                    onConfirmed()
                }
                is MiraiLinkResult.Error -> state.update { it.copy(error = errorFor(result.error) { confirmReset(onConfirmed) }) }
            }
        } finally {
            state.update { it.copy(isLoading = false) }
        }
    }

    fun dismissSuccessDialog(onDismissed: () -> Unit) {
        resetState()
        onDismissed()
    }

    private fun resetState() {
        state.value = PasswordResetState()
    }
}
