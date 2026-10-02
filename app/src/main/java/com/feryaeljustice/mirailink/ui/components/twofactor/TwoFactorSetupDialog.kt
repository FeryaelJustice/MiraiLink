package com.feryaeljustice.mirailink.ui.components.twofactor

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.components.molecules.QrCodeImage
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
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
    onConfirm: () -> Unit,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val secretCopiedText = stringResource(R.string.two_factor_secret_copied)

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
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                otpUrl?.let {
                    QrCodeImage(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        content = it,
                        size = 300.dp,
                    )
                }
                MiraiLinkText(
                    text = stringResource(R.string.enter_code_from_app),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
                MiraiLinkOutlinedTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    placeholder = {
                        MiraiLinkText(text = stringResource(R.string.code_placeholder))
                    },
                    maxLines = 1,
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                )
                MiraiLinkText(
                    text = stringResource(R.string.or_use_secret_code),
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall,
                )
                MiraiLinkText(
                    text = base32,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.pointerInput(base32) {
                        detectTapGestures(
                            onLongPress = {
                                coroutineScope.launch {
                                    clipboard.setClipEntry(ClipData.newPlainText("2FA Secret", base32).toClipEntry())
                                    Toast.makeText(context, secretCopiedText, Toast.LENGTH_SHORT).show()
                                }
                            },
                        )
                    },
                )
                if (recoveryCodes.isNotEmpty()) {
                    MiraiLinkText(
                        text = stringResource(R.string.recovery_codes),
                        fontWeight = FontWeight.SemiBold,
                    )
                    recoveryCodes.forEach {
                        MiraiLinkText(
                            text = it,
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if (isLoading) {
                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TwoFactorSetupDialogPreview() {
    MiraiLinkTheme {
        TwoFactorSetupDialog(
            otpUrl = null,
            base32 = "JBSWY3DPEHPK3PXP",
            recoveryCodes = listOf("1234-5678", "8765-4321"),
            code = "123456",
            isLoading = false,
            onCodeChange = {},
            onDismiss = {},
            onConfirm = {},
        )
    }
}
