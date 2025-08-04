package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigureTwoFactorViewModel @Inject constructor(
    private val setup2FAUseCase: SetupTwoFactorUseCase,
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase,
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase
) :
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

    private val _verify2FACode = MutableStateFlow("")
    val verify2FACode = _verify2FACode.asStateFlow()

    private val _isConfigure2FALoading = MutableStateFlow(false)
    val isConfigure2FALoading = _isConfigure2FALoading.asStateFlow()

    private val _errorString = MutableStateFlow<String?>(null)
    val errorString = _errorString.asStateFlow()

    fun onCodeChanged(newCode: String) {
        _verify2FACode.value = newCode
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            checkTwoFacStatus()
        }
    }

    fun launchSetupDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = setup2FAUseCase()) {
                is MiraiLinkResult.Success -> {
                    result.data.enabled?.let { _isTwoFactorEnabled.value = it }
                    _otpUrl.value = result.data.otpAuthUrl
                    _base32.value = result.data.baseCode.orEmpty()
                    _recoveryCodes.value = result.data.recoveryCodes ?: emptyList()
                    _showSetupDialog.value = true
                }

                is MiraiLinkResult.Error -> {
                    _errorString.value = result.message
                    _showSetupDialog.value = false
                }
            }
        }
    }

    fun confirmCode() {
        _isConfigure2FALoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            // Implementación futura
            val result = verifyTwoFactorUseCase(code = _verify2FACode.value)
            // manejar respuesta
            when (result) {
                is MiraiLinkResult.Success -> {
                    _isConfigure2FALoading.value = false
                    _showSetupDialog.value = false
                    checkTwoFacStatus()
                }

                is MiraiLinkResult.Error -> {
                    _errorString.value = result.message
                    _isConfigure2FALoading.value = false
                    _showSetupDialog.value = false
                }
            }
        }
    }

    suspend fun checkTwoFacStatus() {
        when (val res = getTwoFactorStatusUseCase()) {
            is MiraiLinkResult.Success -> {
                _isTwoFactorEnabled.value = res.data
            }

            is MiraiLinkResult.Error -> {
                _isTwoFactorEnabled.value = false
            }
        }
    }

    fun dismissDialog() {
        _showSetupDialog.value = false
        _verify2FACode.value = ""
        _otpUrl.value = null
        _base32.value = ""
        _recoveryCodes.value = emptyList()
        _errorString.value = null
        _isConfigure2FALoading.value = false
    }
}