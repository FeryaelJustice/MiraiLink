package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConfigureTwoFactorViewModel @Inject constructor(private val setup2FAUseCase: SetupTwoFactorUseCase) :
    ViewModel() {
    private val _isTwoFactorEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isTwoFactorEnabled = _isTwoFactorEnabled.asStateFlow()

    private val _showSetupDialog = MutableStateFlow(false)
    val showSetupDialog = _showSetupDialog.asStateFlow()

    private val _otpUrl = MutableStateFlow<String?>(null)
    val otpUrl = _otpUrl.asStateFlow()

    private val _base32 = MutableStateFlow("")
    val base32 = _base32.asStateFlow()

    private val _recoveryCodes = MutableStateFlow<List<String>>(emptyList())
    val recoveryCodes = _recoveryCodes.asStateFlow()

    private val _code = MutableStateFlow("")
    val code = _code.asStateFlow()

    private val _isConfigure2FALoading = MutableStateFlow(false)
    val isConfigure2FALoading = _isConfigure2FALoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun onCodeChanged(newCode: String) {
        _code.value = newCode
    }

    fun launchSetupDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = setup2FAUseCase()
            when (result) {
                is MiraiLinkResult.Success -> {
                    result.data.enabled?.let { _isTwoFactorEnabled.value = it }
                    _otpUrl.value = result.data.otpAuthUrl
                    _base32.value = result.data.baseCode.orEmpty()
                    _recoveryCodes.value = result.data.recoveryCodes ?: emptyList()
                    withContext(Dispatchers.Main) {
                        _showSetupDialog.value = true
                    }
                }

                is MiraiLinkResult.Error -> {
                    _error.value = result.message
                    withContext(Dispatchers.Main) {
                        _showSetupDialog.value = false
                    }
                }
            }
        }
    }

    fun confirmCode() {
        _isConfigure2FALoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            // Implementación futura
            // val result = verify2FAUseCase(_code.value)
            // manejar respuesta
            _isConfigure2FALoading.value = false
            _showSetupDialog.value = false
        }
    }

    fun dismissDialog() {
        _showSetupDialog.value = false
        _code.value = ""
        _otpUrl.value = null
        _base32.value = ""
        _recoveryCodes.value = emptyList()
    }
}