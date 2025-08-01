package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val requestResetUseCase: Lazy<RequestPasswordResetUseCase>,
    private val confirmResetUseCase: Lazy<ConfirmPasswordResetUseCase>
) : ViewModel() {

    data class PasswordResetState(
        val step: Int = 1,
        val email: String = "",
        val token: String = "",
        val newPassword: String = "",
        val error: String? = null
    )

    private val _state = MutableStateFlow(PasswordResetState())
    val state = _state.asStateFlow()

    fun initEmail(initialEmail: String) = _state.update { it.copy(email = initialEmail) }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, error = null) }
    }

    fun onTokenChanged(token: String) {
        _state.update { it.copy(token = token, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(newPassword = password, error = null) }
    }

    fun requestReset() = viewModelScope.launch {
        val email = state.value.email
        val result = withContext(Dispatchers.IO) {
            requestResetUseCase.get()(email)
        }

        when (result) {
            is MiraiLinkResult.Success -> _state.update { it.copy(step = 2) }
            is MiraiLinkResult.Error -> _state.update { it.copy(error = result.message) }
        }
    }

    fun confirmReset(onConfirmed: () -> Unit) = viewModelScope.launch {
        val s = state.value

        val result = withContext(Dispatchers.IO) {
            confirmResetUseCase.get()(s.email, s.token, s.newPassword)
        }

        when (result) {
            is MiraiLinkResult.Success -> {
                resetState()
                onConfirmed()
            }

            is MiraiLinkResult.Error -> _state.update { it.copy(error = result.message) }
        }
    }

    private fun resetState() {
        _state.value = PasswordResetState()
    }
}