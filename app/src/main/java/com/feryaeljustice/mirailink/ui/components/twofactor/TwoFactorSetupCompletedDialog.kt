package com.feryaeljustice.mirailink.ui.components.twofactor

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import kotlinx.coroutines.launch

@Composable
fun TwoFactorSetupCompletedDialog(
    recoveryCodes: List<String> = emptyList(),
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val codesCopiedText = stringResource(R.string.two_factor_codes_copied)

    MiraiLinkDialog(
        title = stringResource(R.string.two_factor_setup_completed_title),
        onDismiss = onDismiss,
        onAccept = onDismiss,
        acceptText = stringResource(R.string.done),
        showCancelButton = false,
        iconContent = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        messageContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.two_factor_setup_completed_message),
                    style = MaterialTheme.typography.bodyMedium,
                )

                if (recoveryCodes.isNotEmpty()) {
                    MiraiLinkText(
                        text = stringResource(R.string.two_factor_backup_codes_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    MiraiLinkText(
                        text = stringResource(R.string.two_factor_backup_codes_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        recoveryCodes.chunked(2).forEach { rowCodes ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                rowCodes.forEach { code ->
                                    MiraiLinkText(
                                        text = code,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                val allCodes = recoveryCodes.joinToString("\n")
                                clipboard.setClipEntry(ClipData.newPlainText("2FA Recovery Codes", allCodes).toClipEntry())
                                Toast.makeText(context, codesCopiedText, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.two_factor_copy_all_codes),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        },
    )
}
