package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText
import com.feryaeljustice.mirailink.ui.error.asString
import kotlinx.coroutines.delay

/**
 * Compact inline error card banner.
 * Designed to sit above categories in lists (e.g. Settings) as one more card.
 * Occupies only the height necessary for its message and action button.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun MiraiLinkErrorContent(
    modifier: Modifier = Modifier,
    error: UiError,
    onAction: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.error.copy(alpha = 0.4f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.error),
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp),
                    )
                }
                MiraiLinkText(
                    text = error.message.asString(),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                )
            }
            Button(
                onClick = onAction,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onError,
                )
                Spacer(modifier = Modifier.width(8.dp))
                MiraiLinkText(
                    text = error.actionLabel.asString(),
                    color = MaterialTheme.colorScheme.onError,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

/**
 * Floating snackbar/alert banner for full-screen card experiences (Discovery, Explore feeds).
 * Floats above the content without replacing or occluding the whole screen,
 * automatically dismisses after [autoDismissSeconds], and provides a clear action button.
 */
@Composable
fun MiraiLinkErrorSnackbar(
    modifier: Modifier = Modifier,
    error: UiError,
    onAction: () -> Unit,
    autoDismissSeconds: Int = 5,
    onDismiss: (() -> Unit)? = null,
) {
    var isVisible by remember(error) { mutableStateOf(true) }

    LaunchedEffect(error) {
        if (autoDismissSeconds > 0) {
            delay(autoDismissSeconds * 1000L)
            isVisible = false
            onDismiss?.invoke()
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = modifier.zIndex(99f),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.5f),
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = stringResource(R.string.error),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
                MiraiLinkText(
                    text = error.message.asString(),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                )
                Button(
                    onClick = {
                        isVisible = false
                        onAction()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp),
                ) {
                    MiraiLinkText(
                        text = error.actionLabel.asString(),
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
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

@Preview(showBackground = true)
@Composable
private fun MiraiLinkErrorSnackbarPreview() {
    MiraiLinkErrorSnackbar(
        error = UiError(
            message = UiText.Resource(R.string.error),
            actionLabel = UiText.Resource(R.string.action_retry),
            recovery = ErrorRecovery.RETRY,
        ),
        onAction = {},
    )
}
