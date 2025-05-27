package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestPasswordResetUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val requestReset: RequestPasswordResetUseCase,
    private val confirmReset: ConfirmPasswordResetUseCase
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
        _state.value = state.value.copy(email = email, error = null)
    }

    fun onTokenChanged(token: String) {
        _state.value = state.value.copy(token = token, error = null)
    }

    fun onPasswordChanged(password: String) {
        _state.value = state.value.copy(newPassword = password, error = null)
    }

    fun requestReset() = viewModelScope.launch {
        when (val result = requestReset(_state.value.email)) {
            is MiraiLinkResult.Success -> _state.value = state.value.copy(step = 2)
            is MiraiLinkResult.Error -> _state.value = state.value.copy(error = result.message)
        }
    }

    fun confirmReset(onConfirmed: () -> Unit) = viewModelScope.launch {
        val s = _state.value
        when (val result = confirmReset(s.email, s.token, s.newPassword)) {
            is MiraiLinkResult.Success -> {
                resetState()
                onConfirmed()
            }

            is MiraiLinkResult.Error -> _state.value = s.copy(error = result.message)
        }
    }

    private fun resetState() {
        _state.value = PasswordResetState(
            step = 1, email = "",
            token = "",
            newPassword = "",
            error = null
        )
    }
}