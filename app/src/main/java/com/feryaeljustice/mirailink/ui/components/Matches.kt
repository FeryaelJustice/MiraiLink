package com.feryaeljustice.mirailink.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.utils.extensions.debounceClickable
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

@Composable
fun MatchCard(
    user: MatchUserViewEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(72.dp)
            .debounceClickable { onClick() }
    ) {
        Box {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            if (user.isBoosted) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = "Boosted",
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(2.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.nickname.ifBlank { user.username },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}