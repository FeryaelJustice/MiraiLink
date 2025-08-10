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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.components.molecules.QrCodeImage

@Composable
fun TwoFactorSetupDialog(
    modifier: Modifier = Modifier,
    otpUrl: String?,
    base32: String,
    recoveryCodes: List<String>,
    code: String,
    isLoading: Boolean,
    onCodeChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    MiraiLinkDialog(
        modifier = modifier,
        onDismiss = onDismiss,
        onAccept = onConfirm,
        onCancel = onDismiss,
        acceptText = stringResource(R.string.verify),
        cancelText = stringResource(R.string.cancel),
        title = stringResource(R.string.setup_two_factor),
        messageContent = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                otpUrl?.let {
                    QrCodeImage(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        content = it,
                        size = 300.dp
                    )
                }
                MiraiLinkText(
                    text = stringResource(R.string.enter_code_from_app),
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
                MiraiLinkText(
                    text = stringResource(R.string.or_use_secret_code),
                    color = Color.Gray,
                    fontSize = 13.sp
                )
                MiraiLinkText(
                    text = base32,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                if (recoveryCodes.isNotEmpty()) {
                    MiraiLinkText(
                        text = stringResource(R.string.recovery_codes),
                        fontWeight = FontWeight.SemiBold
                    )
                    recoveryCodes.forEach {
                        MiraiLinkText(
                            text = it,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                if (isLoading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    )
}