package com.feryaeljustice.mirailink.ui.components.topbars

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@Composable
fun ChatTopBar(
    modifier: Modifier = Modifier,
    receiverName: String? = null,
    receiverUrlPhoto: String? = null,
    onReportClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                )
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MiraiLinkIconButton(
            modifier = Modifier.padding(horizontal = 2.dp),
            onClick = {
                onBackClick()
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(receiverUrlPhoto)
                .crossfade(true)
                .placeholder(drawableResId = R.drawable.logomirailink)
                .build(),
            contentDescription = stringResource(R.string.chat_top_bar_user_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        MiraiLinkText(
            text = receiverName?.superCapitalize() ?: stringResource(R.string.unknown),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.weight(1f))
        MiraiLinkIconButton(
            onClick = onReportClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_report),
                contentDescription = stringResource(R.string.report_user),
                tint = MaterialTheme.colorScheme.onError
            )
        }
    }
}