package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText
import com.feryaeljustice.mirailink.ui.error.asString

/** Renders an error message and its required call-to-action button. */
@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkErrorContent(
    modifier: Modifier = Modifier,
    error: UiError,
    onAction: () -> Unit,
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        MiraiLinkText(
            text = error.message.asString(),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
        MiraiLinkButton(
            onClick = onAction,
            modifier = Modifier.fillMaxWidth(),
        ) {
            MiraiLinkText(text = error.actionLabel.asString())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MiraiLinkErrorContentPreview() {
    MiraiLinkErrorContent(
        error = UiError(
            message = UiText.Resource(R.string.error),
            actionLabel = UiText.Resource(R.string.action_retry),
            recovery = ErrorRecovery.RETRY,
        ),
        onAction = {},
    )
}

