package com.feryaeljustice.mirailink.ui.components.twofactor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog

@Composable
fun TwoFactorStatusDialog(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onEnable: () -> Unit,
    onDisable: () -> Unit,
) {
    val statusColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    MiraiLinkDialog(
        title = stringResource(R.string.configure_two_factor),
        onDismiss = onDismiss,
        onAccept = if (enabled) onDismiss else onEnable,
        onCancel = if (enabled) onDisable else onDismiss,
        acceptText = stringResource(if (enabled) R.string.close else R.string.activate_two_factor),
        cancelText = stringResource(if (enabled) R.string.disable_two_factor else R.string.close),
        iconContent = {
            Icon(
                imageVector = if (enabled) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = statusColor,
            )
        },
        messageContent = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = if (enabled) Icons.Default.CheckCircle else Icons.Default.Close,
                    contentDescription = null,
                    tint = statusColor,
                )
                MiraiLinkText(
                    text = stringResource(if (enabled) R.string.two_factor_enabled_message else R.string.two_factor_disabled_message),
                    color = statusColor,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
    )
}
