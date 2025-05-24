package com.feryaeljustice.mirailink.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (matches.isEmpty()) {
                item {
                    Text(
                        text = "No tienes chats",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                items(matches) { user ->
                    MatchCard(user = user, onClick = { onNavigateToChat(user.id) })
                }
            }
        }
    }
}

@Composable
fun MatchCard(
    user: MatchUserViewEntity,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
            text = user.username,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}