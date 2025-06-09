package com.feryaeljustice.mirailink.ui.components.match

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.viewentities.MatchUserViewEntity

@Composable
fun MatchesRow(
    modifier: Modifier = Modifier,
    matches: List<MatchUserViewEntity>,
    onNavigateToChat: (String) -> Unit
) {
    Column(modifier = modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 16.dp))) {
        Text(
            text = "Matches",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row {
            if (matches.isEmpty()) {
                Text(
                    text = "No tienes matches",
                    style = MaterialTheme.typography.bodySmall,
                )
            } else {
                matches.forEach { user ->
                    MatchCard(
                        user = user,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .onVisibilityChanged(callback = {
                                Log.d("MatchesRow", "Visibility changed: ${user.username}")
                            })
                            .onFirstVisible(callback = {
                                Log.d("MatchesRow", "First visible: ${user.username}")
                            }),
                        onClick = { onNavigateToChat(user.id) })
                }
            }
        }
    }
}