package com.feryaeljustice.mirailink.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel.AuthUiState
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onLogin: (String?) -> Unit,
    onRegister: (String?) -> Unit,
    onRequestPasswordReset: (String) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var isLogin by remember { mutableStateOf(true) }
    var loginByUsername by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun resetAuthUiState() {
        username = ""
        email = ""
        password = ""
        confirmPassword = ""
        passwordVisible = false

        usernameError = null
        emailError = null
        passwordError = null
        confirmPasswordError = null
    }

    LaunchedEffect(Unit) {
        sessionViewModel.showHideTopBar(true)
        sessionViewModel.showHideBottomBar(false)
        sessionViewModel.enableDisableTopBar(false)
        sessionViewModel.enableDisableBottomBar(false)
        sessionViewModel.hideTopBarSettingsIcon()
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .then(
                if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                } else {
                    Modifier
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MiraiLinkTextButton(
                modifier = Modifier
                    .focusRequester(focusRequester),
                onClick = {
                    isLogin = !isLogin
                    viewModel.resetUiState()
                    resetAuthUiState()
                },
                text = if (isLogin) stringResource(R.string.auth_screen_register) else stringResource(
                    R.string.auth_screen_login
                ),
            )
        }

        MiraiLinkOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = if (loginByUsername && isLogin) username else email,
            onValueChange = {
                if (loginByUsername && isLogin) {
                    username = it
                    usernameError = null
                } else {
                    email = it
                    emailError = null
                }
            },
            maxLines = 1,
            label = if (loginByUsername && isLogin) stringResource(R.string.auth_screen_text_field_username) else stringResource(
                R.string.auth_screen_text_field_email
            ),
            isError = if (loginByUsername && isLogin) usernameError != null else emailError != null,
            supportingText = if (loginByUsername && isLogin) usernameError else emailError,
            trailingIcon = {
                if (isLogin) {
                    val icon =
                        if (loginByUsername) R.drawable.ic_user else R.drawable.ic_email
                    IconButton(onClick = { loginByUsername = !loginByUsername }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null)
                    }
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_email),
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
        )

        if (!isLogin) {
            Spacer(modifier = Modifier.height(8.dp))

            MiraiLinkOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                maxLines = 1,
                label = stringResource(R.string.auth_screen_text_field_username),
                isError = usernameError != null,
                supportingText = usernameError,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user),
                        contentDescription = null
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Next) }),
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        MiraiLinkOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            maxLines = 1,
            label = stringResource(R.string.auth_screen_text_field_password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(id = icon), contentDescription = null)
                }
            },
            isError = passwordError != null,
            supportingText = passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = if (isLogin) ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Next) },
                onDone = {
                    focusManager.clearFocus()
                    viewModel.login(email, username, password)
                }),
        )

        if (!isLogin) {
            Spacer(modifier = Modifier.height(8.dp))
            MiraiLinkOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                maxLines = 1,
                label = stringResource(R.string.auth_screen_text_field_repeat_password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null)
                    }
                },
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    viewModel.register(
                        username,
                        email,
                        password
                    )
                }),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            MiraiLinkTextButton(
                modifier = Modifier
                    .focusRequester(focusRequester),
                onClick = {
                    onRequestPasswordReset(email)
                },
                text = stringResource(R.string.auth_screen_text_btn_forgot_password),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        MiraiLinkButton(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            onClick = {
                var valid = true
                if (isLogin) {
                    if (loginByUsername && username.length < 4) {
                        usernameError = context.getString(R.string.must_have_less_than, 4)
                        valid = false
                    }
                    if (!loginByUsername && (email.length < 5 || !email.contains("@"))) {
                        emailError =
                            context.getString(R.string.must_have_less_than_and_contain, 5, '@')
                        valid = false
                    }
                } else {
                    if (confirmPassword != password) {
                        confirmPasswordError = context.getString(R.string.passwords_do_not_match)
                        valid = false
                    }
                }
                if (password.length < 4) {
                    passwordError = context.getString(R.string.must_have_less_than, 4)
                    valid = false
                }

                if (!valid) return@MiraiLinkButton

                if (isLogin) viewModel.login(email, username, password)
                else viewModel.register(username, email, password)
            },
            content = {
                MiraiLinkText(
                    text = if (isLogin) stringResource(R.string.auth_screen_login) else stringResource(
                        R.string.auth_screen_register
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        )

        when (state) {
            is AuthUiState.Success -> {
                val userId = (state as AuthUiState.Success).userId
                resetAuthUiState()
                if (isLogin) onLogin(userId) else onRegister(userId)
            }

            is AuthUiState.Error -> {
                val error = state as AuthUiState.Error
                Log.e("AuthScreen", error.message)
                MiraiLinkText(error.message, color = MaterialTheme.colorScheme.error)
            }

            is AuthUiState.IsAuthenticated -> {
                val userId = (state as AuthUiState.IsAuthenticated).userId
                onLogin(userId)
            }

            AuthUiState.Loading -> {
                CircularProgressIndicator()
            }

            AuthUiState.Idle -> Unit
        }
    }
}