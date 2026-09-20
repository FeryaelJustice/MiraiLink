package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@Composable
fun RecoverPasswordScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    email: String,
    onConfirmedRecoverPassword: () -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RecoverPasswordViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    LaunchedEffect(Unit) {
        // La recuperacion sigue fuera de la sesion: mantiene ocultas ambas barras.
        miraiLinkSession.showHideTopBar(false)
        miraiLinkSession.showHideBottomBar(false)
        miraiLinkSession.enableDisableTopBar(false)
        miraiLinkSession.enableDisableBottomBar(false)
        miraiLinkSession.hideTopBarSettingsIcon()
        miraiLinkSession.disableBars()
        viewModel.initEmail(email)
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier =
            modifier
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
        verticalArrangement = Arrangement.Top,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkIconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back),
                    )
                }
                Image(
                    painter = painterResource(R.drawable.logomirailink),
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier.padding(start = 8.dp).height(56.dp),
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    MiraiLinkText(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    MiraiLinkText(
                        text = stringResource(R.string.auth_recover_title),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.28f),
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
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

                uiState.error?.let { error ->
                    Spacer(modifier = Modifier.height(12.dp))
                    MiraiLinkErrorContent(error = error, onAction = viewModel::performErrorAction)
                }
            }
        }
    }
}
