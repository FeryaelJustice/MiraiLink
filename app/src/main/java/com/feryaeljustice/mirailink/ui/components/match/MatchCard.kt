package com.feryaeljustice.mirailink.ui.components.match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.utils.extensions.debounceClickable

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    userAvatarUrl: String? = TEMPORAL_PLACEHOLDER_PICTURE_URL,
    userIsBoosted: Boolean = false,
    userUsername: String = "",
    userNickname: String = "",
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
                model = userAvatarUrl,
                contentDescription = stringResource(R.string.user_avatar),
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            if (userIsBoosted) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bolt),
                    contentDescription = stringResource(R.string.boosted),
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

        MiraiLinkText(
            text = userNickname.ifBlank { userUsername },
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}