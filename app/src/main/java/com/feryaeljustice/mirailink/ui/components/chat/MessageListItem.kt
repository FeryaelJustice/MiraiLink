package com.feryaeljustice.mirailink.ui.components.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Suppress("ktlint:standard:function-naming")
@Composable
fun MessageListItem(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    chatUserId: String? = null,
    chatAvatarUrl: String = "",
    chatUsername: String = "",
    chatNickname: String = "",
    chatIsGroup: Boolean = false,
    chatIsBoosted: Boolean = false,
    chatLastMessage: String = "",
    chatReadsPending: Int = 0,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .then(if (chatUserId != null && !chatIsGroup) Modifier.clickable { onClick(chatUserId) } else Modifier)
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                .data(chatAvatarUrl.ifBlank { R.drawable.logomirailink })
                .error(R.drawable.logomirailink)
                .build(),
            contentDescription = stringResource(R.string.user_avatar),
            contentScale = ContentScale.Crop,
            modifier =
                Modifier
                    .size(56.dp)
                    .clip(CircleShape),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MiraiLinkText(
                    text = chatNickname.ifBlank { chatUsername },
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (chatIsBoosted) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_bolt),
                        contentDescription = stringResource(R.string.boosted),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            MiraiLinkText(
                text = chatLastMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (chatReadsPending > 0) {
            Badge(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                modifier = Modifier.padding(end = 8.dp),
            ) {
                MiraiLinkText(
                    text =
                        if (chatReadsPending >
                            99
                        ) {
                            stringResource(R.string.more_than_ninetynine)
                        } else {
                            chatReadsPending.toString()
                        },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MessageListItemPreview() {
    MiraiLinkTheme {
        MessageListItem(
            chatNickname = "Alice",
            chatUsername = "alice_w",
            chatLastMessage = "Hey, are you free tonight?",
            chatIsBoosted = true,
            chatReadsPending = 3,
            onClick = {},
        )
    }
}
