package com.feryaeljustice.mirailink.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.screens.auth.AuthViewModel.AuthUiState
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onLogin: (String?) -> Unit,
    onRegister: (String?) -> Unit,
    onRequestPasswordReset: (String) -> Unit,
) {
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

    fun clearForm() {
        username = ""
        password = ""
        confirmPassword = ""
        usernameError = null
        passwordError = null
        confirmPasswordError = null
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        sessionViewModel.showTopBarSettingsIcon()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    isLogin = !isLogin
                    usernameError = null
                    passwordError = null
                    confirmPasswordError = null
                }, modifier = Modifier
                    .focusRequester(focusRequester)
            ) {
                Text(if (isLogin) "Registrarse" else "Iniciar sesión")
            }
        }

        OutlinedTextField(
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
            singleLine = true,
            maxLines = 1,
            label = { Text(if (loginByUsername && isLogin) "Usuario" else "Email") },
            isError = if (loginByUsername && isLogin) usernameError != null else emailError != null,
            supportingText = if (loginByUsername && isLogin) usernameError?.let {
                {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else emailError?.let {
                {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        if (!isLogin) {
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "Usuario") },
                isError = usernameError != null,
                supportingText = usernameError?.let {
                    {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            singleLine = true,
            maxLines = 1,
            label = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon =
                    if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(id = icon), contentDescription = null)
                }
            },
            isError = passwordError != null,
            supportingText = passwordError?.let {
                {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
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
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        if (!isLogin) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = null
                },
                singleLine = true,
                maxLines = 1,
                label = { Text("Repetir contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(id = icon), contentDescription = null)
                    }
                },
                isError = confirmPasswordError != null,
                supportingText = confirmPasswordError?.let {
                    {
                        Text(
                            it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    onRequestPasswordReset(email)
                }, modifier = Modifier
                    .focusRequester(focusRequester)
            ) {
                Text("¿Has olvidado tu contraseña?")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                var valid = true
                if (isLogin) {
                    if (loginByUsername && username.length < 4) {
                        usernameError = "Debe tener al menos 4 caracteres"
                        valid = false
                    }
                    if (!loginByUsername && (email.length < 5 || !email.contains("@"))) {
                        emailError = "Debe tener al menos 5 caracteres y contener @"
                        valid = false
                    }
                } else {
                    if (confirmPassword != password) {
                        confirmPasswordError = "Las contraseñas no coinciden"
                        valid = false
                    }
                }
                if (password.length < 4) {
                    passwordError = "Debe tener al menos 4 caracteres"
                    valid = false
                }

                if (!valid) return@Button

                if (isLogin) viewModel.login(email, username, password)
                else viewModel.register(username, email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        ) {
            Text(if (isLogin) "Iniciar sesión" else "Registrarse")
        }

        when (state) {
            is AuthUiState.Success -> {
                val userId = (state as AuthUiState.Success).userId
                clearForm()
                Text("Éxito")
                if (isLogin) onLogin(userId) else onRegister(userId)
            }

            is AuthUiState.Error -> {
                val error = state as AuthUiState.Error
                Log.d("AuthScreen", "Error: ${error.message}")
                Text("Error: ${error.message}", color = MaterialTheme.colorScheme.error)
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