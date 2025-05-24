package com.feryaeljustice.mirailink.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    horizontal = 16.dp,
                    vertical = 16.dp
                )
            ),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        stickyHeader {
            Text(
                text = "Chats",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        if (chats.isEmpty()) {
            item {
                Text(
                    text = "No tienes chats",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            items(chats) { chat ->
                MessageListItem(chat = chat, onClick = {
                    onNavigateToChat(it)
                })
            }
        }
    }
}