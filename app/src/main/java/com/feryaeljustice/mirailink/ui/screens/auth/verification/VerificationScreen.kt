package com.feryaeljustice.mirailink.ui.screens.auth.verification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@Composable
fun VerificationDialog(
    userId: String,
    onVerified: () -> Unit,
    onClose: () -> Unit,
    viewModel: VerificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    AlertDialog(
        onDismissRequest = {},
        confirmButton = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = { MiraiLinkText(text = stringResource(R.string.error_verification_required)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (uiState.step) {
                    1 -> {
                        MiraiLinkText(text = stringResource(R.string.request_email_verification))
                        Spacer(modifier = Modifier.height(8.dp))
                        MiraiLinkButton(onClick = { viewModel.requestCode(userId) }) {
                            MiraiLinkText(
                                text = stringResource(R.string.send_code),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                    2 -> {
                        MiraiLinkOutlinedTextField(
                            value = uiState.token,
                            onValueChange = viewModel::onTokenChanged,
                            label = stringResource(R.string.code),
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        MiraiLinkButton(onClick = { viewModel.confirmCode(userId, onFinish = onVerified) }) {
                            MiraiLinkText(
                                text = stringResource(R.string.verify),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
                uiState.error?.let { error ->
                    MiraiLinkErrorContent(error = error, onAction = viewModel::performErrorAction)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                MiraiLinkText(text = stringResource(R.string.close))
            }
        },
    )
}
