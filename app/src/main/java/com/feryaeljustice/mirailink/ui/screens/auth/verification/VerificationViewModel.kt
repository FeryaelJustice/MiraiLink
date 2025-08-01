package com.feryaeljustice.mirailink.ui.screens.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
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
class VerificationViewModel @Inject constructor(
    private val checkIsVerifiedUseCase: Lazy<CheckIsVerifiedUseCase>,
    private val requestCodeUseCase: Lazy<RequestVerificationCodeUseCase>,
    private val confirmCodeUseCase: Lazy<ConfirmVerificationCodeUseCase>,
) : ViewModel() {

    data class VerificationState(
        val step: Int = 1,
        val token: String = "",
        val error: String? = null
    )

    private val _state = MutableStateFlow(VerificationState())
    val state = _state.asStateFlow()

    fun onTokenChanged(token: String) {
        _state.update { it.copy(token = token, error = null) }
    }

    fun checkUserIsVerified(onFinish: () -> Unit) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) {
            checkIsVerifiedUseCase.get()()
        }

        when (result) {
            is MiraiLinkResult.Success -> {
                if (result.data) {
                    resetState()
                    onFinish()
                }
            }

            is MiraiLinkResult.Error -> {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    fun requestCode(userId: String) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) {
            requestCodeUseCase.get()(userId, "email")
        }

        when (result) {
            is MiraiLinkResult.Success -> _state.update { it.copy(step = 2) }
            is MiraiLinkResult.Error -> _state.update { it.copy(error = result.message) }
        }
    }


    fun confirmCode(userId: String, onFinish: () -> Unit) = viewModelScope.launch {
        val result = withContext(Dispatchers.IO) {
            confirmCodeUseCase.get()(userId, _state.value.token, "email")
        }

        when (result) {
            is MiraiLinkResult.Success -> {
                resetState()
                onFinish()
            }

            is MiraiLinkResult.Error -> _state.update { it.copy(error = result.message) }
        }
    }

    private fun resetState() {
        _state.value = VerificationState()
    }
}