package com.feryaeljustice.mirailink.ui.components.topbars

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.capsule.CrystalCapsuleIcon
import com.feryaeljustice.mirailink.ui.components.media.CrystalPhoto

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    onLongPressOnImage: (String) -> Unit,
    onReportClick: () -> Unit,
    onBackClick: () -> Unit,
    onAvatarClick: (() -> Unit)? = null,
    photoPresentation: PhotoPresentation? = null,
    unlockPulse: Int = 0,
    receiverName: String? = null,
    receiverUrlPhoto: String? = null,
    capsule: CrystalCapsule? = null,
    ownUserId: String? = null,
    onCapsuleClick: (() -> Unit)? = null,
) {
    val currentLongPressHandler by rememberUpdatedState(newValue = onLongPressOnImage)
    val currentAvatarClickHandler by rememberUpdatedState(newValue = onAvatarClick)

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.surfaceVariant,
                    RoundedCornerShape(4.dp),
                )
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MiraiLinkIconButton(
            modifier = Modifier.padding(horizontal = 2.dp),
            onClick = {
                onBackClick()
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }

        CrystalPhoto(
            unlockPulse = unlockPulse,
            photoPresentation = photoPresentation,
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(receiverUrlPhoto)
                    .crossfade(true)
                    .placeholder(drawableResId = R.drawable.logomirailink)
                    .build(),
            contentDescription = stringResource(R.string.chat_top_bar_user_photo),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .pointerInput(receiverUrlPhoto) {
                        detectTapGestures(
                            onTap = {
                                currentAvatarClickHandler?.invoke()
                            },
                            onLongPress = {
                                receiverUrlPhoto?.let { _ ->
                                    currentLongPressHandler(receiverUrlPhoto)
                                }
                            },
                        )
                    },
        )

        Spacer(modifier = Modifier.width(12.dp))
        MiraiLinkText(
            text = receiverName?.superCapitalize() ?: stringResource(R.string.unknown),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.weight(1f))

        // Icono especial de Crystal Capsule a la izquierda de reportar
        if (capsule != null && onCapsuleClick != null) {
            val hasPendingToAnswer = capsule.question != null &&
                !capsule.question.completed &&
                capsule.question.authorId.isNotEmpty() &&
                capsule.question.authorId != ownUserId

            CrystalCapsuleIcon(
                progress = capsule.progress,
                isRevealed = capsule.status == "revealed",
                hasPendingToAnswer = hasPendingToAnswer,
                onClick = onCapsuleClick,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        MiraiLinkIconButton(
            onClick = onReportClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_report),
                contentDescription = stringResource(R.string.report_user),
                tint = MaterialTheme.colorScheme.onError,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatTopBarPreview() {
    ChatTopBar(
        onLongPressOnImage = {},
        onReportClick = {},
        onBackClick = {},
        receiverName = "Sakura",
    )
}
