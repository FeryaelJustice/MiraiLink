package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecoverPasswordViewModel(
    private val requestResetUseCase: RequestPasswordResetUseCase,
    private val confirmResetUseCase: ConfirmPasswordResetUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    data class PasswordResetState(
        val step: Int = 1,
        val email: String = "",
        val token: String = "",
        val newPassword: String = "",
        val error: String? = null,
    )

    val state: StateFlow<PasswordResetState>
    field = MutableStateFlow<PasswordResetState>(PasswordResetState())

    fun initEmail(initialEmail: String) = state.update { it.copy(email = initialEmail) }

    fun onEmailChanged(email: String) {
        state.update { it.copy(email = email, error = null) }
    }

    fun onTokenChanged(token: String) {
        state.update { it.copy(token = token, error = null) }
    }

    fun onPasswordChanged(password: String) {
        state.update { it.copy(newPassword = password, error = null) }
    }

    fun requestReset() =
        viewModelScope.launch {
            val email = state.value.email
            val result =
                withContext(ioDispatcher) {
                    requestResetUseCase(email)
                }

            when (result) {
                is MiraiLinkResult.Success -> state.update { it.copy(step = 2) }
                is MiraiLinkResult.Error -> state.update { it.copy(error = result.message) }
            }
        }

    fun confirmReset(onConfirmed: () -> Unit) =
        viewModelScope.launch {
            val s = state.value

            val result =
                withContext(ioDispatcher) {
                    confirmResetUseCase(s.email, s.token, s.newPassword)
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    resetState()
                    onConfirmed()
                }

                is MiraiLinkResult.Error -> state.update { it.copy(error = result.message) }
            }
        }

    private fun resetState() {
        state.value = PasswordResetState()
    }
}
