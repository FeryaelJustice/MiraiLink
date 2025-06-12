package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            when (logoutUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(Dispatchers.Main) {
                        _logoutSuccess.emit(true)
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
}
