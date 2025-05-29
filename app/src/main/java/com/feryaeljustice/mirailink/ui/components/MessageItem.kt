package com.feryaeljustice.mirailink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.domain.model.ChatMessage
import com.feryaeljustice.mirailink.domain.util.formatTimestamp

@Composable
fun MessageItem(
    message: ChatMessage,
    isOwnMessage: Boolean
) {
    val arrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    val backgroundColor =
        if (isOwnMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val textColor =
        if (isOwnMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onTertiary
    val timeText = formatTimestamp(message.timestamp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = arrangement
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = message.content,
                color = textColor,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = timeText,
                color = textColor.copy(alpha = 0.7f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.align(Alignment.Bottom)
            )
        }
    }
}
