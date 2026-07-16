package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    val error: StateFlow<UiError?>
        field = MutableStateFlow<UiError?>(null)

    private val _deleteSuccess = MutableSharedFlow<Boolean>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    fun logout(onFinish: () -> Unit) {
        setRecoveryAction { logout(onFinish) }
        error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = logoutUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(false)
                        error.value = result.error.toUiError()
                    }
                }
            }
        }
    }

    fun deleteAccount(onFinish: () -> Unit) {
        setRecoveryAction { deleteAccount(onFinish) }
        error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = deleteAccountUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(false)
                        error.value = result.error.toUiError()
                    }
                }
            }
        }
    }
}
