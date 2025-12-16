package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef")
@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    miraiLinkSession: GlobalMiraiLinkSession,
    email: String,
    onConfirmedRecoverPassword: () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    LaunchedEffect(Unit) {
        miraiLinkSession.showHideTopBar(true)
        miraiLinkSession.showHideBottomBar(false)
        miraiLinkSession.enableDisableTopBar(false)
        miraiLinkSession.enableDisableBottomBar(false)
        miraiLinkSession.hideTopBarSettingsIcon()
        miraiLinkSession.disableBars()
        viewModel.initEmail(email)
    }

    val uiState by viewModel.state.collectAsState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        when (uiState.step) {
            1 -> {
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = stringResource(R.string.recover_password_screen_mail),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = viewModel::requestReset,
                ) {
                    MiraiLinkText(
                        text = stringResource(R.string.send_code),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }

            2 -> {
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.token,
                    onValueChange = viewModel::onTokenChanged,
                    label = stringResource(R.string.code),
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.newPassword,
                    onValueChange = viewModel::onPasswordChanged,
                    label = stringResource(R.string.new_password),
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(
                    onClick = {
                        viewModel.confirmReset(onConfirmed = onConfirmedRecoverPassword)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    MiraiLinkText(
                        text = stringResource(R.string.confirm),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        }

        if (uiState.error != null) MiraiLinkText(text = uiState.error!!, color = Color.Red)
    }
}
