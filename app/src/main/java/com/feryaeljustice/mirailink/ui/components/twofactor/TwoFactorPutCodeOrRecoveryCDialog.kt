package com.feryaeljustice.mirailink.ui.components.twofactor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog

@Composable
fun TwoFactorPutCodeOrRecoveryCDialog(
    code: String,
    isLoading: Boolean,
    onCodeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    MiraiLinkDialog(
        onDismiss = null,
        onAccept = onConfirm,
        onCancel = onDismiss,
        acceptText = stringResource(R.string.accept),
        cancelText = stringResource(R.string.cancel),
        title = stringResource(R.string.disable_two_factor),
        messageContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.enter_code_from_app_or_use_recovery_code),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
                MiraiLinkOutlinedTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    placeholder = {
                        MiraiLinkText(text = stringResource(R.string.code_placeholder))
                    },
                    maxLines = 1,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                if (isLoading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}