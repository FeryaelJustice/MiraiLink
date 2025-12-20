package com.feryaeljustice.mirailink.ui.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry

@Suppress("ktlint:standard:function-naming")
@Composable
fun ChatList(
    chats: List<ChatPreviewViewEntry>,
    onNavigateToChat: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier.padding(
                PaddingValues(
                    horizontal = 16.dp,
                    vertical = 16.dp,
                ),
            ),
    ) {
        MiraiLinkText(
            text = stringResource(R.string.chats),
            style = MaterialTheme.typography.titleLarge,
        )

        if (chats.isEmpty()) {
            MiraiLinkText(
                text = stringResource(R.string.chats_empty),
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        } else {
            chats.forEach { chat ->
                MessageListItem(
                    chatUserId = chat.userId,
                    chatAvatarUrl = chat.avatarUrl,
                    chatUsername = chat.username,
                    chatNickname = chat.nickname,
                    chatIsBoosted = chat.isBoosted,
                    chatLastMessage = chat.lastMessage,
                    onClick = {
                        onNavigateToChat(it)
                    },
                )
            }
        }
    }
}
