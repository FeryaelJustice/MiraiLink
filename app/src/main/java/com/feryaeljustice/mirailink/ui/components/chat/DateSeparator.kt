/**
 * @author Feryael Justice
 * @date 02/08/2024
 */
package com.feryaeljustice.mirailink.ui.components.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import java.util.Locale

@Suppress("ktlint:standard:function-naming")
@Composable
fun DateSeparator(
    modifier: Modifier = Modifier,
    date: String,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        Text(
            text = date.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DateSeparatorPreview() {
    MiraiLinkTheme {
        DateSeparator(
            date = "Today",
        )
    }
}
