package com.feryaeljustice.mirailink.ui.screens.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerified
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationViewModel @Inject constructor(
    private val checkIsVerified: CheckIsVerified,
    private val requestCode: RequestVerificationCodeUseCase,
    private val confirmCode: ConfirmVerificationCodeUseCase,
) : ViewModel() {

    data class VerificationState(
        val step: Int = 1,
        val token: String = "",
        val error: String? = null
    )

    private val _state = MutableStateFlow(VerificationState())
    val state = _state.asStateFlow()

    fun onTokenChanged(token: String) {
        _state.value = state.value.copy(token = token, error = null)
    }

    fun checkUserIsVerified(onFinish: () -> Unit) = viewModelScope.launch {
        when (val result = checkIsVerified()) {
            is MiraiLinkResult.Success -> {
                if (result.data) {
                    resetState()
                    onFinish()
                }
            }

            is MiraiLinkResult.Error -> _state.value = state.value.copy(error = result.message)
        }
    }

    fun requestCode(userId: String) = viewModelScope.launch {
        when (val result = requestCode(userId, "email")) {
            is MiraiLinkResult.Success -> _state.value = state.value.copy(step = 2)
            is MiraiLinkResult.Error -> _state.value = state.value.copy(error = result.message)
        }
    }

    fun confirmCode(userId: String, onFinish: () -> Unit) = viewModelScope.launch {
        when (val result = confirmCode(userId, _state.value.token, "email")) {
            is MiraiLinkResult.Success -> {
                resetState()
                onFinish()
            }

            is MiraiLinkResult.Error -> _state.value = state.value.copy(error = result.message)
        }
    }

    private fun resetState() {
        _state.value = VerificationState(
            step = 1,
            token = "",
            error = null
        )
    }
}