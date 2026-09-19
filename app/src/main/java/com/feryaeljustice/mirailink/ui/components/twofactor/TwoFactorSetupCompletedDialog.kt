package com.feryaeljustice.mirailink.ui.components.twofactor

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog

@Composable
fun TwoFactorSetupCompletedDialog(onDismiss: () -> Unit) {
    MiraiLinkDialog(
        title = stringResource(R.string.two_factor_setup_completed_title),
        message = stringResource(R.string.two_factor_setup_completed_message),
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
    )
}
