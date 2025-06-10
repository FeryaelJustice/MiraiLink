package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    sessionViewModel: GlobalSessionViewModel,
    email: String,
    onConfirmedRecoverPassword: () -> Unit,
) {
    LaunchedEffect(Unit) {
        sessionViewModel.showHideTopBar(true)
        sessionViewModel.showHideBottomBar(false)
        sessionViewModel.enableDisableTopBar(false)
        sessionViewModel.enableDisableBottomBar(false)
        sessionViewModel.hideTopBarSettingsIcon()
        viewModel.initEmail(email)
    }

    val uiState by viewModel.state.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        when (uiState.step) {
            1 -> {
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = "Correo electrónico",
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = viewModel::requestReset,
                ) {
                    MiraiLinkText(
                        text = "Enviar código",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            2 -> {
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.token,
                    onValueChange = viewModel::onTokenChanged,
                    label = "Código de verificación",
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.newPassword,
                    onValueChange = viewModel::onPasswordChanged,
                    label = "Nueva contraseña",
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(
                    onClick = {
                        viewModel.confirmReset(onConfirmed = onConfirmedRecoverPassword)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { MiraiLinkText(text = "Confirmar", color = MaterialTheme.colorScheme.onPrimary) }
            }
        }

        if (uiState.error != null) MiraiLinkText(text = uiState.error!!, color = Color.Red)
    }
}