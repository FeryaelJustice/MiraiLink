package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.DisableTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.SetupTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.VerifyTwoFactorUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ConfigureTwoFactorViewModel(
    private val setup2FAUseCase: SetupTwoFactorUseCase,
    private val verifyTwoFactorUseCase: VerifyTwoFactorUseCase,
    private val getTwoFactorStatusUseCase: GetTwoFactorStatusUseCase,
    private val disableTwoFactorUseCase: DisableTwoFactorUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {
    val isTwoFactorEnabled: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val showSetupDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val showStatusDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val showSetupCompletedDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val isCheckingStatus: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(true)

    val isConfigure2FALoading: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val isStartingSetup: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val showDisableTwoFactorDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val isDisable2FALoading: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val otpUrl: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    val base32: StateFlow<String>
        field = MutableStateFlow<String>("")

    val recoveryCodes: StateFlow<List<String>>
        field = MutableStateFlow<List<String>>(emptyList())

    val verify2FACode: StateFlow<String>
        field = MutableStateFlow<String>("")

    val disable2FACode: StateFlow<String>
        field = MutableStateFlow<String>("")

    val errorString: StateFlow<UiError?>
        field = MutableStateFlow<UiError?>(null)

    fun onlyCheckTwoFacStatusWithIO(userID: String?) {
        viewModelScope.launch(ioDispatcher) {
            checkTwoFacStatus(userID = userID)
        }
    }

    fun onSetupTwoFactorCodeChanged(newCode: String) {
        verify2FACode.value = newCode
    }

    fun onDisableTwoFactorCodeChanged(newCode: String) {
        disable2FACode.value = newCode
    }

    fun launchSetupTwoFactorDialog() {
        setRecoveryAction(::launchSetupTwoFactorDialog)
        errorString.value = null
        isStartingSetup.value = true
        showSetupDialog.value = true
        viewModelScope.launch(ioDispatcher) {
            when (val result = setup2FAUseCase()) {
                is MiraiLinkResult.Success -> {
                    isStartingSetup.value = false
                    errorString.value = null
                    result.data.enabled?.let { isTwoFactorEnabled.value = it }
                    otpUrl.value = result.data.otpAuthUrl
                    base32.value = result.data.baseCode.orEmpty()
                    recoveryCodes.value = result.data.recoveryCodes ?: emptyList()
                    showSetupDialog.value = true
                }

                is MiraiLinkResult.Error -> {
                    isStartingSetup.value = false
                    errorString.value = result.error.toUiError()
                }
            }
        }
    }

    fun launchDisableTwoFactorDialog() {
        if (isTwoFactorEnabled.value) {
            showDisableTwoFactorDialog.value = true
        }
    }

    fun confirmSetupTwoFactor(userID: String?) {
        setRecoveryAction { confirmSetupTwoFactor(userID) }
        errorString.value = null
        isConfigure2FALoading.value = true
        viewModelScope.launch(ioDispatcher) {
            // Implementación futura
            val result = verifyTwoFactorUseCase(code = verify2FACode.value)
            // manejar respuesta
            when (result) {
                is MiraiLinkResult.Success -> {
                    errorString.value = null
                    isConfigure2FALoading.value = false
                    showSetupDialog.value = false
                    showSetupCompletedDialog.value = true
                    checkTwoFacStatus(userID = userID, showDialog = false)
                }

                is MiraiLinkResult.Error -> {
                    errorString.value = result.error.toUiError()
                    isConfigure2FALoading.value = false
                    showSetupDialog.value = false
                }
            }
        }
    }

    fun confirmDisableTwoFactor(userID: String?) {
        setRecoveryAction { confirmDisableTwoFactor(userID) }
        errorString.value = null
        isDisable2FALoading.value = true
        viewModelScope.launch(ioDispatcher) {
            when (
                val result =
                    disableTwoFactorUseCase(codeOrRecoveryCode = disable2FACode.value)
            ) {
                is MiraiLinkResult.Success -> {
                    errorString.value = null
                    isDisable2FALoading.value = false
                    showDisableTwoFactorDialog.value = false
                    checkTwoFacStatus(userID = userID)
                }

                is MiraiLinkResult.Error -> {
                    errorString.value = result.error.toUiError()
                    isDisable2FALoading.value = false
                    showDisableTwoFactorDialog.value = false
                }
            }
        }
    }

    suspend fun checkTwoFacStatus(userID: String?, showDialog: Boolean = true) {
        userID?.let { usID ->
            when (val res = getTwoFactorStatusUseCase(userID = usID)) {
                is MiraiLinkResult.Success -> {
                    isTwoFactorEnabled.value = res.data
                    errorString.value = null
                    isCheckingStatus.value = false
                    if (showDialog) showStatusDialog.value = true
                }

                is MiraiLinkResult.Error -> {
                    isTwoFactorEnabled.value = false
                    setRecoveryAction { onlyCheckTwoFacStatusWithIO(userID) }
                    errorString.value = res.error.toUiError()
                    isCheckingStatus.value = false
                }
            }
        }
    }

    fun dismissSetupTwoFactorDialog() {
        showSetupDialog.value = false
        isStartingSetup.value = false
        isDisable2FALoading.value = false

        verify2FACode.value = ""
        otpUrl.value = null
        base32.value = ""
        recoveryCodes.value = emptyList()

        errorString.value = null
    }

    fun dismissStatusDialog() {
        showStatusDialog.value = false
    }

    fun dismissSetupCompletedDialog() {
        showSetupCompletedDialog.value = false
    }

    fun launchActivationFromStatus() {
        showStatusDialog.value = false
        launchSetupTwoFactorDialog()
    }

    fun dismissDisableTwoFactorDialog() {
        showDisableTwoFactorDialog.value = false
        isConfigure2FALoading.value = false

        disable2FACode.value = ""

        errorString.value = null
    }
}
