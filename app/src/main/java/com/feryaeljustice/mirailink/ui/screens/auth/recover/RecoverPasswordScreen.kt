package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    sessionViewModel: GlobalSessionViewModel,
    email: String,
    onConfirmedRecoverPassword: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sessionViewModel.showTopBarSettingsIcon()
        viewModel.initEmail(email)
    }

    val uiState by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (uiState.step) {
            1 -> {
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = viewModel::requestReset,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Enviar código") }
            }

            2 -> {
                OutlinedTextField(
                    value = uiState.token,
                    onValueChange = viewModel::onTokenChanged,
                    label = { Text("Código de verificación") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.newPassword,
                    onValueChange = viewModel::onPasswordChanged,
                    label = { Text("Nueva contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        viewModel.confirmReset(onConfirmed = onConfirmedRecoverPassword)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Confirmar") }
            }
        }

        if (uiState.error != null) Text(uiState.error!!, color = Color.Red)
    }
}