package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.di.IoDispatcher
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfigureTwoFactorViewModel
    @Inject
    constructor(
        private val setup2FAUseCase: SetupTwoFactorUseCase,
        private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase,
        private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase,
        private val disableTwoFactorUseCase: DisableTwoFactorUseCase,
        @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        private val _isTwoFactorEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val isTwoFactorEnabled = _isTwoFactorEnabled.asStateFlow()

        private val _showSetupDialog = MutableStateFlow(false)
        val showSetupDialog = _showSetupDialog.asStateFlow()

        private val _isConfigure2FALoading = MutableStateFlow(false)
        val isConfigure2FALoading = _isConfigure2FALoading.asStateFlow()
        private val _showDisableTwoFactorDialog = MutableStateFlow(false)
        val showDisableTwoFactorDialog = _showDisableTwoFactorDialog.asStateFlow()
        private val _isDisable2FALoading = MutableStateFlow(false)
        val isDisable2FALoading = _isDisable2FALoading.asStateFlow()

        private val _otpUrl = MutableStateFlow<String?>(null)
        val otpUrl = _otpUrl.asStateFlow()

        private val _base32 = MutableStateFlow("")
        val base32 = _base32.asStateFlow()

        private val _recoveryCodes = MutableStateFlow<List<String>>(emptyList())
        val recoveryCodes = _recoveryCodes.asStateFlow()

        private val _verify2FACode = MutableStateFlow("")
        val verify2FACode = _verify2FACode.asStateFlow()
        private val _disable2FACode = MutableStateFlow("")
        val disable2FACode = _disable2FACode.asStateFlow()

        private val _errorString = MutableStateFlow<String?>(null)
        val errorString = _errorString.asStateFlow()

        fun onlyCheckTwoFacStatusWithIO(userID: String?) {
            viewModelScope.launch(ioDispatcher) {
                checkTwoFacStatus(userID = userID)
            }
        }

        fun onSetupTwoFactorCodeChanged(newCode: String) {
            _verify2FACode.value = newCode
        }

        fun onDisableTwoFactorCodeChanged(newCode: String) {
            _disable2FACode.value = newCode
        }

        fun launchSetupTwoFactorDialog() {
            viewModelScope.launch(ioDispatcher) {
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

        fun launchDisableTwoFactorDialog() {
            if (_isTwoFactorEnabled.value) {
                _showDisableTwoFactorDialog.value = true
            }
        }

        fun confirmSetupTwoFactor(userID: String?) {
            _isConfigure2FALoading.value = true
            viewModelScope.launch(ioDispatcher) {
                // Implementación futura
                val result = verifyTwoFactorUseCase(code = _verify2FACode.value)
                // manejar respuesta
                when (result) {
                    is MiraiLinkResult.Success -> {
                        _isConfigure2FALoading.value = false
                        _showSetupDialog.value = false
                        checkTwoFacStatus(userID = userID)
                    }

                    is MiraiLinkResult.Error -> {
                        _errorString.value = result.message
                        _isConfigure2FALoading.value = false
                        _showSetupDialog.value = false
                    }
                }
            }
        }

        fun confirmDisableTwoFactor(userID: String?) {
            _isDisable2FALoading.value = true
            viewModelScope.launch(ioDispatcher) {
                when (
                    val result =
                        disableTwoFactorUseCase(codeOrRecoveryCode = _disable2FACode.value)
                ) {
                    is MiraiLinkResult.Success -> {
                        _isDisable2FALoading.value = false
                        _showDisableTwoFactorDialog.value = false
                        checkTwoFacStatus(userID = userID)
                    }

                    is MiraiLinkResult.Error -> {
                        _errorString.value = result.message
                        _isDisable2FALoading.value = false
                        _showDisableTwoFactorDialog.value = false
                    }
                }
            }
        }

        suspend fun checkTwoFacStatus(userID: String?) {
            userID?.let { usID ->
                when (val res = getTwoFactorStatusUseCase(userID = usID)) {
                    is MiraiLinkResult.Success -> {
                        _isTwoFactorEnabled.value = res.data
                    }

                    is MiraiLinkResult.Error -> {
                        _isTwoFactorEnabled.value = false
                    }
                }
            }
        }

        fun dismissSetupTwoFactorDialog() {
            _showSetupDialog.value = false
            _isDisable2FALoading.value = false

            _verify2FACode.value = ""
            _otpUrl.value = null
            _base32.value = ""
            _recoveryCodes.value = emptyList()

            _errorString.value = null
        }

        fun dismissDisableTwoFactorDialog() {
            _showDisableTwoFactorDialog.value = false
            _isConfigure2FALoading.value = false

            _disable2FACode.value = ""

            _errorString.value = null
        }
    }
