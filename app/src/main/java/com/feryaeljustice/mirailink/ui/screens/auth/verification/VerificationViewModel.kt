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
        val error: UiError? = null,
    )

    val state: StateFlow<VerificationState>
        field = MutableStateFlow<VerificationState>(VerificationState())

    fun onTokenChanged(token: String) {
        state.update { it.copy(token = token, error = null) }
    }

    fun checkUserIsVerified(onFinished: (isVerified: Boolean) -> Unit): Job =
        viewModelScope.launch {
            setRecoveryAction { checkUserIsVerified(onFinished) }
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
                    state.update { it.copy(error = result.error.toUiError()) }
                }
            }
        }

    fun requestCode(userId: String): Job =
        viewModelScope.launch {
            setRecoveryAction { requestCode(userId) }
            val result =
                withContext(ioDispatcher) {
                    requestCodeUseCase(userId, "email")
                }

            when (result) {
                is MiraiLinkResult.Success -> state.update { it.copy(step = 2) }
                is MiraiLinkResult.Error -> state.update { it.copy(error = result.error.toUiError()) }
            }
        }

    fun confirmCode(
        userId: String,
        onFinish: () -> Unit,
    ): Job = viewModelScope.launch {
        setRecoveryAction { confirmCode(userId, onFinish) }
        val result =
            withContext(ioDispatcher) {
                confirmCodeUseCase(userId, state.value.token, "email")
            }

        when (result) {
            is MiraiLinkResult.Success -> {
                resetState()
                onFinish()
            }

            is MiraiLinkResult.Error -> {
                state.update { it.copy(error = result.error.toUiError()) }
            }
        }
    }

    private fun resetState() {
        state.value = VerificationState()
    }
}
