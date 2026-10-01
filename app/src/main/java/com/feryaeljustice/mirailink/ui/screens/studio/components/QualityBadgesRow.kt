package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.domain.model.studio.QualityBadge

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QualityBadgesRow(
    badges: List<QualityBadge>,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        badges.forEach { badge ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + scaleIn(),
            ) {
                QualityBadgeChip(badge = badge)
            }
        }
    }
}

@Composable
fun QualityBadgeChip(
    badge: QualityBadge,
    modifier: Modifier = Modifier,
) {
    val borderColor = when (badge) {
        QualityBadge.OPTIMAL_LIGHTING -> Color(0xFF00E5FF)
        QualityBadge.AUTHENTIC_SMILE -> Color(0xFFFF4081)
        QualityBadge.DIRECT_GAZE -> Color(0xFF7C4DFF)
        QualityBadge.CENTERED_FRAME -> Color(0xFF00E676)
        QualityBadge.EYES_OPEN -> Color(0xFF00B0FF)
        QualityBadge.HIGH_RESOLUTION -> Color(0xFFFFD600)
        QualityBadge.VISUAL_ART_APPROVED -> Color(0xFFFF9100)
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xCC080D1A))
            .border(1.dp, borderColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = borderColor,
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = stringResource(badge.titleRes),
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.Monospace,
        )
    }
}
