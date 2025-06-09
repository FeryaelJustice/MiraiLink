package com.feryaeljustice.mirailink.ui.components.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.viewentities.ChatPreviewViewEntity

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<ChatPreviewViewEntity>,
    onNavigateToChat: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(
            PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            )
        )
    ) {
        Text(
            text = "Chats",
            style = MaterialTheme.typography.titleMedium,
        )

        if (chats.isEmpty()) {
            Text(
                text = "No tienes chats",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            chats.forEach { chat ->
                MessageListItem(chat = chat, onClick = {
                    onNavigateToChat(it)
                })
            }
        }
    }
}