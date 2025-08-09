package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: Lazy<LogoutUseCase>,
    private val deleteAccountUseCase: Lazy<DeleteAccountUseCase>,
) : ViewModel() {

    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()


    private val _deleteSuccess = MutableSharedFlow<Boolean>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    fun logout(onFinish: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (logoutUseCase.get()()) {
                is MiraiLinkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        _logoutSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        _logoutSuccess.emit(false)
                    }
                }
            }
        }
    }

    fun deleteAccount(onFinish: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (deleteAccountUseCase.get()()) {
                is MiraiLinkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        _deleteSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(Dispatchers.Main) {
                        _deleteSuccess.emit(false)
                    }
                }
            }
        }
    }
}
