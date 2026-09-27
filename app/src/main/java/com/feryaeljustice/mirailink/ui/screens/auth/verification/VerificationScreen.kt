package com.feryaeljustice.mirailink.ui.screens.auth.verification

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
fun VerificationScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    userId: String,
    onVerified: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    token: String = "",
    viewModel: VerificationViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        miraiLinkSession.hideBars()
        miraiLinkSession.disableBars()
        viewModel.init(userId, token)
    }

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
                        text = stringResource(R.string.verification_screen_title),
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
                MiraiLinkText(
                    text = stringResource(R.string.verification_screen_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(16.dp))

                MiraiLinkOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.token,
                    onValueChange = viewModel::onTokenChanged,
                    label = stringResource(R.string.code),
                    maxLines = 1,
                )

                if (uiState.error == null) {
                    Spacer(modifier = Modifier.height(16.dp))

                    MiraiLinkButton(
                        onClick = { viewModel.confirmCode(userId, onFinish = onVerified) },
                        enabled = uiState.token.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.verify),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    MiraiLinkErrorContent(error = uiState.error!!, onAction = viewModel::performErrorAction)
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef")
@Composable
fun VerificationDialog(
    userId: String,
    onConfirmSendEmail: () -> Unit,
    onClose: () -> Unit,
    viewModel: VerificationViewModel = koinViewModel(),
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { MiraiLinkText(text = stringResource(R.string.error_verification_required)) },
        text = {
            MiraiLinkText(
                text = stringResource(R.string.account_unverified_dialog_message),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        confirmButton = {
            MiraiLinkButton(
                onClick = {
                    viewModel.requestCode(userId)
                    onConfirmSendEmail()
                },
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.send_verification_email),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                MiraiLinkText(text = stringResource(R.string.close))
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
    )
}
