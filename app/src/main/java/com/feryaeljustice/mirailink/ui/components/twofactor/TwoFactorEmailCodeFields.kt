package com.feryaeljustice.mirailink.ui.components.twofactor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@Composable
fun TwoFactorEmailCodeFields(
    code: String,
    sent: Boolean,
    loading: Boolean,
    onCodeChange: (String) -> Unit,
    onRequest: () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    Column(modifier = modifier) {
        MiraiLinkText(text = stringResource(R.string.two_factor_email_instructions))
        if (sent) MiraiLinkText(text = stringResource(R.string.two_factor_email_sent))
        MiraiLinkOutlinedTextField(
            value = code,
            onValueChange = onCodeChange,
            label = stringResource(R.string.two_factor_email_code),
            enabled = sent && !loading,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
        )
        MiraiLinkButton(onClick = onRequest, enabled = !loading) {
            MiraiLinkText(text = stringResource(R.string.two_factor_email_resend))
        }
        error?.let { MiraiLinkText(text = it) }
    }
}
