package com.feryaeljustice.mirailink.ui.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.domain.model.chat.ChatMessage
import com.feryaeljustice.mirailink.domain.util.formatTimestamp
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@Composable
fun MessageItem(
    message: ChatMessage,
    isOwnMessage: Boolean
) {
    val style = getMessageStyle(isOwnMessage)
    val timeText = remember(message.timestamp) { formatTimestamp(message.timestamp) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = style.alignment
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = style.backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            horizontalAlignment = style.horizontalAlignment
        ) {
            MiraiLinkText(
                text = message.content,
                color = style.textColor,
            )
            MiraiLinkText(
                text = timeText,
                modifier = Modifier.align(Alignment.End),
                color = style.textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

private data class MessageStyle(
    val alignment: Alignment,
    val horizontalAlignment: Alignment.Horizontal,
    val backgroundColor: Color,
    val textColor: Color
)

@Composable
private fun getMessageStyle(isOwnMessage: Boolean): MessageStyle {
    return if (isOwnMessage) {
        MessageStyle(
            alignment = Alignment.CenterEnd,
            horizontalAlignment = Alignment.End,
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary
        )
    } else {
        MessageStyle(
            alignment = Alignment.CenterStart,
            horizontalAlignment = Alignment.Start,
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiary
        )
    }
}
