package com.feryaeljustice.mirailink.ui.components.appconfig

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.VersionCheckResult
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog

@Composable
fun ForceUpdateGate(
    result: VersionCheckResult,
    onOpenStore: () -> Unit
) {
    MiraiLinkDialog(
        title = stringResource(R.string.update_required_title),
        message = result.message.ifBlank { stringResource(R.string.update_required_body) },
        onDismiss = {}, // no dejar cerrar
        onAccept = onOpenStore,
        onCancel = null, // sin botón cancelar
        showCancelButton = false, // 🔒 bloqueante
        acceptText = stringResource(R.string.update_now)
    )
}