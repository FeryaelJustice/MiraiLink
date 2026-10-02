package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.domain.model.studio.MetricStatus
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Composable
fun HudMetricGauge(
    modifier: Modifier = Modifier,
    label: String,
    valuePercent: Int,
    status: MetricStatus,
) {
    val animatedPercent by animateFloatAsState(
        targetValue = (valuePercent.coerceIn(0, 100)) / 100f,
        label = "gauge_val",
    )

    val gaugeColor by animateColorAsState(
        targetValue = when (status) {
            MetricStatus.EXCELLENT -> Color(0xFF00E5FF) // Neon Cyan
            MetricStatus.ACCEPTABLE -> Color(0xFFFFD600) // Amber
            MetricStatus.WARNING -> Color(0xFFFF1744) // Neon Red
        },
        label = "gauge_color",
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xB30A0E1A))
            .border(1.dp, gaugeColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            Text(
                text = "$valuePercent%",
                color = gaugeColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0x33FFFFFF)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedPercent)
                    .height(4.dp)
                    .background(gaugeColor),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HudMetricGaugePreview() {
    MiraiLinkTheme {
        HudMetricGauge(
            label = "ENFOQUE",
            valuePercent = 88,
            status = MetricStatus.EXCELLENT,
        )
    }
}
