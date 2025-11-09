package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    private val _deleteSuccess = MutableSharedFlow<Boolean>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    fun logout(onFinish: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (logoutUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(false)
                    }
                }
            }
        }
    }

    fun deleteAccount(onFinish: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            when (deleteAccountUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(false)
                    }
                }
            }
        }
    }
}
