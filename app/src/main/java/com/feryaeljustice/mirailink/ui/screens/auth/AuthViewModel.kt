package com.feryaeljustice.mirailink.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.di.IoDispatcher
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
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val loginUseCase: Lazy<LoginUseCase>,
        private val registerUseCase: Lazy<RegisterUseCase>,
        private val getTwoFactorStatusUseCase: Lazy<GetTwoFactorStatusUseCase>,
        private val loginVerifyTwoFactorLastStepUseCase: Lazy<LoginVerifyTwoFactorLastStepUseCase>,
        private val analytics: AnalyticsTracker,
        private val crash: CrashReporter,
        private val credentialHelper: CredentialHelper,
        @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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

            object InvalidPassword : AuthFieldError()

            object PasswordsDoNotMatch : AuthFieldError()
        }

        private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
        val state = _state.asStateFlow()

        private val _loginByUsername = MutableStateFlow(true)
        val loginByUsername = _loginByUsername.asStateFlow()

        private val _usernameError = MutableStateFlow<AuthFieldError?>(null)
        val usernameError: StateFlow<AuthFieldError?> = _usernameError.asStateFlow()

        fun resetUsernameError() {
            _usernameError.value = null
        }

        private val _emailError = MutableStateFlow<AuthFieldError?>(null)
        val emailError: StateFlow<AuthFieldError?> = _emailError.asStateFlow()

        fun resetEmailError() {
            _emailError.value = null
        }

        private val _passwordError = MutableStateFlow<AuthFieldError?>(null)
        val passwordError: StateFlow<AuthFieldError?> = _passwordError.asStateFlow()

        fun resetPasswordError() {
            _passwordError.value = null
        }

        private val _confirmPasswordError = MutableStateFlow<AuthFieldError?>(null)
        val confirmPasswordError: StateFlow<AuthFieldError?> = _confirmPasswordError.asStateFlow()

        fun resetConfirmPasswordError() {
            _confirmPasswordError.value = null
        }

        private val _loginToken = MutableStateFlow<String?>(null)
//    val loginToken = _loginToken.asStateFlow()

        private val _userId = MutableStateFlow<String?>(null)
        val userId = _userId.asStateFlow()

        private val _showTwoFactorLastStepDialog = MutableStateFlow(false)
        val showTwoFactorLastStepDialog = _showTwoFactorLastStepDialog.asStateFlow()
        private val _twoFactorLastStepDialogIsLoading = MutableStateFlow(false)
        val twoFactorLastStepDialogIsLoading = _twoFactorLastStepDialogIsLoading.asStateFlow()
        private val _twoFactorCode = MutableStateFlow("")
        val twoFactorCode = _twoFactorCode.asStateFlow()

        fun autofillCredentials(onFound: (String, String) -> Unit) {
            viewModelScope.launch {
                credentialHelper.getSavedPasswordCredential()?.let {
                    onFound(it.first, it.second)
                }
            }
        }

        fun toggleLoginBy() {
            _loginByUsername.value = !_loginByUsername.value
        }

        fun login(
            email: String,
            username: String,
            password: String,
            onSaveSession: (String, String) -> Unit,
        ) {
            _state.value = AuthUiState.Loading
            viewModelScope.launch {
                if ((email.isNotBlank() || username.isNotBlank()) && password.isNotBlank()) {
                    credentialHelper.savePasswordCredential(
                        email = email.ifBlank { username },
                        password = password,
                    )
                }

                val result =
                    withContext(ioDispatcher) {
                        loginUseCase.get()(email, username, password)
                    }

                handleAuthResult(result, onSaveTheSession = { userId, token ->
                    onSaveSession(userId, token)
                })
            }
        }

        fun register(
            username: String,
            email: String,
            password: String,
            onSaveSession: (String, String) -> Unit,
        ) {
            _state.value = AuthUiState.Loading
        /*    viewModelScope.launch {
                val result = withContext(ioDispatcher) {
                    registerUseCase.get()(username, email, password)
                }

                handleAuthResult(result)
            }*/
            viewModelScope.launch {
                if ((email.isNotBlank() || username.isNotBlank()) && password.isNotBlank()) {
                    credentialHelper.savePasswordCredential(
                        email = email.ifBlank { username },
                        password = password,
                    )
                }

                val result =
                    withContext(ioDispatcher) {
                        registerUseCase.get()(username, email, password)
                    }
                var usId = ""
                var tokn = ""
                handleAuthResult(result, onSaveTheSession = { userId, token ->
                    usId = userId
                    tokn = token
                })
                onSaveSession(usId, tokn)
            }
        }

        private suspend fun handleAuthResult(
            result: MiraiLinkResult<String>,
            onSaveTheSession: (String, String) -> Unit,
        ) {
            when (result) {
                is MiraiLinkResult.Success -> {
                    var usID: String? = null

                    _loginToken.value = result.data
                    _loginToken.value?.let { tok ->
                        usID = extractUserId(tok)
                        _userId.value = usID
                    }

                    usID?.let { usuID ->
                        onLoginSuccess(userId = usuID)
                        // Check 2fa is enabled to show dialog
                        when (
                            val twoFactorResult =
                                getTwoFactorStatusUseCase.get()(userID = usuID)
                        ) {
                            is MiraiLinkResult.Success -> {
                                val isTwoFactorEnabled = twoFactorResult.data
                                _showTwoFactorLastStepDialog.value = isTwoFactorEnabled
                                if (!isTwoFactorEnabled) {
                                    completeAuth(
                                        userId = _userId.value,
                                        token = _loginToken.value,
                                        onSaveSession = onSaveTheSession,
                                    )
                                }
                            }

                            is MiraiLinkResult.Error -> {
                                _showTwoFactorLastStepDialog.value = false
                            }
                        }

//                completeAuth(userId = userId, token = token)
                    }
                }

                is MiraiLinkResult.Error -> {
                    _state.value = AuthUiState.Error(result.message, result.exception)
                    onLoginError(result.exception ?: Throwable(result.message))
                }
            }
        }

        fun completeAuth(
            userId: String?,
            token: String?,
            onSaveSession: (String, String) -> Unit,
        ) {
            // Do login / register (for the ui to manage)
            if (userId == null || token == null) {
                _state.value = AuthUiState.Error("No se pudo extraer el ID del usuario")
                return
            }
            _state.value = AuthUiState.Success
            onSaveSession(userId, token)
        }

        // 2FA (si procede)
        fun dismissTwoFactorDiag() {
            _showTwoFactorLastStepDialog.value = false
        }

        fun confirmTwoFactorDiag(onSaveTheSession: (String, String) -> Unit) {
            val userID = _userId.value
            if (userID == null) {
                _state.value = AuthUiState.Error("No existe el ID del usuario")
                return
            }
            if (_twoFactorCode.value.isBlank()) {
                _state.value = AuthUiState.Error("El código de dos factores no puede estar vacío")
                return
            }
            viewModelScope.launch(ioDispatcher) {
                when (
                    val twoFactorLoginVerify =
                        loginVerifyTwoFactorLastStepUseCase.get()(
                            userId = userID,
                            code = _twoFactorCode.value,
                        )
                ) {
                    is MiraiLinkResult.Error -> {
                        resetTwoFaDiag()
                        _state.value = AuthUiState.Error(twoFactorLoginVerify.message)
                    }

                    is MiraiLinkResult.Success -> {
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

        fun onCodeChangeTwoFactorDiag(code: String) {
            _twoFactorCode.value = code
        }

        fun resetScreenVMState() {
            _state.value = AuthUiState.Idle
//        _loginToken.value = null
//        _userId.value = null
        }

        fun resetTwoFaDiag() {
            _showTwoFactorLastStepDialog.value = false
            _twoFactorCode.value = ""
            _twoFactorLastStepDialogIsLoading.value = false
        }

        fun onLoginSuccess(userId: String) {
            analytics.setUserId(userId)
            analytics.logEvent("login_success")
        }

        fun onLoginError(e: Throwable) {
            crash.recordNonFatal(e)
            analytics.logEvent("login_error")
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
            _usernameError.value = null
            _emailError.value = null
            _passwordError.value = null
            _confirmPasswordError.value = null

            var isValid = true

            // Lógica de Registro
            if (!isLogin) {
                if (username.length < 4) {
                    _usernameError.value = AuthFieldError.MinLength(4)
                    isValid = false
                }
                if (!email.isEmailValid()) {
                    _emailError.value = AuthFieldError.InvalidEmail
                    isValid = false
                }
                if (confirmPassword != password) {
                    _confirmPasswordError.value = AuthFieldError.PasswordsDoNotMatch
                    isValid = false
                }
            }
            // Lógica de Login
            else {
                if (_loginByUsername.value) {
                    if (username.length < 4) {
                        _usernameError.value = AuthFieldError.MinLength(4)
                        isValid = false
                    }
                } else { // Login con email
                    if (!email.isEmailValid()) {
                        _emailError.value = AuthFieldError.InvalidEmail
                        isValid = false
                    }
                }
            }

            // Validaciones comunes
            if (!password.isPasswordValid()) {
                _passwordError.value =
                    AuthFieldError.MinLength(4) // Cuando se pase a usar el regex en el ispasswordvalid, aqui poner el InvalidPassword
                isValid = false
            }

            return isValid
        }
    }
