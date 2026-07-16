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
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText

/** Resolves a [UiText] while a Compose resource context is available. */
@Composable
fun UiText.asString(): String =
    when (this) {
        is UiText.Resource -> stringResource(id, *args.toTypedArray())
    }

/** Renders an error message and its required call-to-action button. */
@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkErrorContent(
    error: UiError,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
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
