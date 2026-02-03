package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.core.JwtUtils.extractUserId
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.usecase.auth.LoginUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.RegisterUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.GetTwoFactorStatusUseCase
import com.feryaeljustice.mirailink.domain.usecase.auth.two_factor.LoginVerifyTwoFactorLastStepUseCase
import com.feryaeljustice.mirailink.domain.util.CredentialHelper
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.isEmailValid
import com.feryaeljustice.mirailink.domain.util.isPasswordValid
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AuthViewModel(
    private val loginUseCase: Lazy<LoginUseCase>,
    private val registerUseCase: Lazy<RegisterUseCase>,
    private val getTwoFactorStatusUseCase: Lazy<GetTwoFactorStatusUseCase>,
    private val loginVerifyTwoFactorLastStepUseCase: Lazy<LoginVerifyTwoFactorLastStepUseCase>,
    private val analytics: Lazy<AnalyticsTracker>,
    private val crash: Lazy<CrashReporter>,
    private val credentialHelper: Lazy<CredentialHelper>,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    sealed class AuthUiState {
        object Idle : AuthUiState()

        object Loading : AuthUiState()

        object Success : AuthUiState()

        data class IsAuthenticated(
            val userId: String?,
        ) : AuthUiState()

        data class Error(
            val message: String,
            val exception: Throwable? = null,
        ) : AuthUiState()
    }

    // Sealed class para representar errores de validación de campos de autenticación.
    sealed class AuthFieldError {
        data class MinLength(
            val min: Int,
        ) : AuthFieldError()

        object InvalidEmail : AuthFieldError()

        // object InvalidPassword : AuthFieldError()

        object PasswordsDoNotMatch : AuthFieldError()
    }

    val state: StateFlow<AuthUiState>
        field = MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val loginByUsername: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(true)

    val usernameError: StateFlow<AuthFieldError?>
        field = MutableStateFlow<AuthFieldError?>(null)

    val emailError: StateFlow<AuthFieldError?>
        field = MutableStateFlow<AuthFieldError?>(null)

    val passwordError: StateFlow<AuthFieldError?>
        field = MutableStateFlow<AuthFieldError?>(null)

    val confirmPasswordError: StateFlow<AuthFieldError?>
        field = MutableStateFlow<AuthFieldError?>(null)


    @Suppress("ktlint:standard:backing-property-naming")
    private val _loginToken = MutableStateFlow<String?>(null)
//    val loginToken = _loginToken.asStateFlow()

    val userId: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    val showTwoFactorLastStepDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val twoFactorLastStepDialogIsLoading: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val twoFactorCode: StateFlow<String>
        field = MutableStateFlow<String>("")

    fun resetUsernameError() {
        usernameError.value = null
    }

    fun resetEmailError() {
        emailError.value = null
    }

    fun resetPasswordError() {
        passwordError.value = null
    }

    fun resetConfirmPasswordError() {
        confirmPasswordError.value = null
    }

    fun autofillCredentials(onFound: (String, String) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            credentialHelper.value.getSavedPasswordCredential()?.let {
                withContext(mainDispatcher) {
                    onFound(it.first, it.second)
                }
            }
        }
    }

    fun toggleLoginBy() {
        viewModelScope.launch(mainDispatcher) {
            loginByUsername.value = !loginByUsername.value
        }
    }

    fun login(
        email: String,
        username: String,
        password: String,
        onSaveSession: (String, String) -> Unit,
    ) {
        viewModelScope.launch(ioDispatcher) {
            withContext(mainDispatcher) {
                state.value = AuthUiState.Loading
            }
            if ((email.isNotBlank() || username.isNotBlank()) && password.isNotBlank()) {
                credentialHelper.value.savePasswordCredential(
                    email = email.ifBlank { username },
                    password = password,
                )
            }

            val result = loginUseCase.value(email, username, password)

            handleAuthResult(result, onSaveTheSession = onSaveSession)
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        onSaveSession: (String, String) -> Unit,
    ) {
        viewModelScope.launch(ioDispatcher) {
            withContext(mainDispatcher) {
                state.value = AuthUiState.Loading
            }
            if ((email.isNotBlank() || username.isNotBlank()) && password.isNotBlank()) {
                credentialHelper.value.savePasswordCredential(
                    email = email.ifBlank { username },
                    password = password,
                )
            } else {
                withContext(mainDispatcher) {
                    state.value = AuthUiState.Error("User/Email and Password fields are mandatory")
                }
                return@launch
            }

            val result = registerUseCase.value(username, email, password)
            handleAuthResult(result, onSaveTheSession = onSaveSession)
        }
    }

    private suspend fun handleAuthResult(
        result: MiraiLinkResult<String>,
        onSaveTheSession: (String, String) -> Unit,
    ) {
        when (result) {
            is MiraiLinkResult.Success -> {
                val token = result.data
                val userIdd = extractUserId(token)

                withContext(mainDispatcher) {
                    _loginToken.value = token
                    userId.value = userIdd
                }

                if (userIdd == null) {
                    withContext(mainDispatcher) {
                        state.value = AuthUiState.Error("No se pudo extraer el ID del usuario")
                    }
                    return
                }

                onLoginSuccess(userId = userIdd)

                // Check 2fa is enabled to show dialog
                when (val twoFactorResult = getTwoFactorStatusUseCase.value(userID = userIdd)) {
                    is MiraiLinkResult.Success -> {
                        val isTwoFactorEnabled = twoFactorResult.data
                        withContext(mainDispatcher) {
                            showTwoFactorLastStepDialog.value = isTwoFactorEnabled
                        }
                        // completeAuth switches to Main internally
                        if (!isTwoFactorEnabled) {
                            completeAuth(
                                userId = userIdd,
                                token = token,
                                onSaveSession = onSaveTheSession,
                            )
                        }
                    }

                    is MiraiLinkResult.Error -> {
                        withContext(mainDispatcher) {
                            showTwoFactorLastStepDialog.value = false
                            state.value =
                                AuthUiState.Error(
                                    twoFactorResult.message,
                                    twoFactorResult.exception,
                                )
                        }
                    }
                }
            }

            is MiraiLinkResult.Error -> {
                withContext(mainDispatcher) {
                    state.value = AuthUiState.Error(result.message, result.exception)
                }
                onLoginError(result.exception ?: Throwable(result.message))
            }
        }
    }

    fun completeAuth(
        userId: String?,
        token: String?,
        onSaveSession: (String, String) -> Unit,
    ) {
        viewModelScope.launch(mainDispatcher) {
            if (userId == null || token == null) {
                state.value = AuthUiState.Error("No se pudo extraer el ID del usuario")
                return@launch
            }
            state.value = AuthUiState.Success
            onSaveSession(userId, token)
        }
    }

    // 2FA (si procede)
    fun dismissTwoFactorDiag() {
        viewModelScope.launch(mainDispatcher) {
            showTwoFactorLastStepDialog.value = false
        }
    }

    fun confirmTwoFactorDiag(onSaveTheSession: (String, String) -> Unit) {
        val userID = userId.value
        if (userID == null) {
            viewModelScope.launch(mainDispatcher) {
                state.value = AuthUiState.Error("No existe el ID del usuario")
            }
            return
        }
        if (twoFactorCode.value.isBlank()) {
            viewModelScope.launch(mainDispatcher) {
                state.value = AuthUiState.Error("El código de dos factores no puede estar vacío")
            }
            return
        }
        viewModelScope.launch(ioDispatcher) {
            withContext(mainDispatcher) {
                twoFactorLastStepDialogIsLoading.value = true
            }
            when (
                val twoFactorLoginVerify =
                    loginVerifyTwoFactorLastStepUseCase.value(
                        userId = userID,
                        code = twoFactorCode.value,
                    )
            ) {
                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        resetTwoFaDiag()
                        state.value = AuthUiState.Error(twoFactorLoginVerify.message)
                    }
                }

                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        resetTwoFaDiag()
                        completeAuth(
                            userId = userID,
                            token = _loginToken.value,
                            onSaveSession = onSaveTheSession,
                        )
                    }
                }
            }
        }
    }

    fun onCodeChangeTwoFactorDiag(code: String) {
        viewModelScope.launch(mainDispatcher) {
            twoFactorCode.value = code
        }
    }

    fun resetScreenVMState() {
        viewModelScope.launch(mainDispatcher) {
            state.value = AuthUiState.Idle
        }
    }

    fun resetTwoFaDiag() {
        viewModelScope.launch(mainDispatcher) {
            showTwoFactorLastStepDialog.value = false
            twoFactorCode.value = ""
            twoFactorLastStepDialogIsLoading.value = false
        }
    }

    fun onLoginSuccess(userId: String) {
        analytics.value.setUserId(userId)
        analytics.value.logEvent("login_success")
    }

    fun onLoginError(e: Throwable) {
        crash.value.recordNonFatal(e)
        analytics.value.logEvent("login_error")
    }

    // Función principal para validar los campos, ahora sin conocer el Context.
    fun validateFields(
        isLogin: Boolean,
        username: String,
        email: String,
        password: String,
        confirmPassword: String = "", // Opcional, solo para registro
    ): Boolean {
        // Reseteamos errores previos
        viewModelScope.launch(mainDispatcher) {
            usernameError.value = null
            emailError.value = null
            passwordError.value = null
            confirmPasswordError.value = null
        }

        var isValid = true

        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            // Lógica de Registro
            @Suppress("ktlint:standard:if-else-wrapping")
            if (!isLogin) {
                if (username.length < 4 || email.isBlank()) {
                    viewModelScope.launch(mainDispatcher) {
                        usernameError.value = AuthFieldError.MinLength(4)
                    }
                    isValid = false
                }
                if (!email.isEmailValid() || email.isBlank()) {
                    viewModelScope.launch(mainDispatcher) {
                        emailError.value = AuthFieldError.InvalidEmail
                    }
                    isValid = false
                }
                if (confirmPassword != password) {
                    viewModelScope.launch(mainDispatcher) {
                        confirmPasswordError.value = AuthFieldError.PasswordsDoNotMatch
                    }
                    isValid = false
                }
            }
            // Lógica de Login
            else {
                if (loginByUsername.value) {
                    if (username.isBlank() || username.length < 4) {
                        viewModelScope.launch(mainDispatcher) {
                            usernameError.value = AuthFieldError.MinLength(4)
                        }
                        isValid = false
                    }
                } else { // Login con email
                    if (!email.isEmailValid() || email.isBlank()) {
                        viewModelScope.launch(mainDispatcher) {
                            emailError.value = AuthFieldError.InvalidEmail
                        }
                        isValid = false
                    }
                }
            }
        }

        // Validaciones comunes
        if (!password.isPasswordValid() || password.isBlank()) {
            viewModelScope.launch(mainDispatcher) {
                passwordError.value =
                    AuthFieldError.MinLength(4)
            } // Cuando se pase a usar el regex en el ispasswordvalid, aqui poner el InvalidPassword
            isValid = false
        }

        return isValid
    }
}
