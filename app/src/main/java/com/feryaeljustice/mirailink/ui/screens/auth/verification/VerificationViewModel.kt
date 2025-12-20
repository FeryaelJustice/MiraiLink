package com.feryaeljustice.mirailink.ui.screens.auth.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.CheckIsVerifiedUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.ConfirmVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.RequestVerificationCodeUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class VerificationViewModel(
    private val checkIsVerifiedUseCase: CheckIsVerifiedUseCase,
    private val requestCodeUseCase: RequestVerificationCodeUseCase,
    private val confirmCodeUseCase: ConfirmVerificationCodeUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    data class VerificationState(
        val step: Int = 1,
        val token: String = "",
        val error: String? = null,
    )

    private val _state = MutableStateFlow(VerificationState())
    val state = _state.asStateFlow()

    fun onTokenChanged(token: String) {
        _state.update { it.copy(token = token, error = null) }
    }

    fun checkUserIsVerified(onFinished: (isVerified: Boolean) -> Unit) =
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    checkIsVerifiedUseCase()
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    onFinished(result.data)
                    if (result.data) {
                        resetState()
                    }
                }

                is MiraiLinkResult.Error -> {
                    _state.update { it.copy(error = result.message) }
                }
            }
        }

    fun requestCode(userId: String) =
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    requestCodeUseCase(userId, "email")
                }

            when (result) {
                is MiraiLinkResult.Success -> _state.update { it.copy(step = 2) }
                is MiraiLinkResult.Error -> _state.update { it.copy(error = result.message) }
            }
        }

    fun confirmCode(
        userId: String,
        onFinish: () -> Unit,
    ) = viewModelScope.launch {
        val result =
            withContext(ioDispatcher) {
                confirmCodeUseCase(userId, _state.value.token, "email")
            }

        when (result) {
            is MiraiLinkResult.Success -> {
                resetState()
                onFinish()
            }

            is MiraiLinkResult.Error -> {
                _state.update { it.copy(error = result.message) }
            }
        }
    }

    private fun resetState() {
        _state.value = VerificationState()
    }
}
