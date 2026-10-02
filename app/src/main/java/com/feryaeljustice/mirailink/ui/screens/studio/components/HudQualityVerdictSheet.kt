package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics
import com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton

@Composable
fun HudQualityVerdictSheet(
    modifier: Modifier = Modifier,
    scanResult: MiraiScanResult,
    onAcceptClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val hasWarnings = scanResult.warnings.isNotEmpty()

    val headerColor = if (hasWarnings) Color(0xFFFFD600) else Color(0xFF00E5FF)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 420.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color(0xF00A0F1E))
            .border(1.dp, headerColor.copy(alpha = 0.5f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
    ) {
        // Cabecera de estado
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = if (hasWarnings) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = headerColor,
                    modifier = Modifier.size(24.dp),
                )
                Text(
                    text = stringResource(R.string.studio_verdict_title),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
            }

        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(if (hasWarnings) R.string.studio_filters_warning else R.string.studio_filters_passed),
            color = headerColor,
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Insignias obtenidas
        if (scanResult.badges.isNotEmpty()) {
            Text(
                text = stringResource(R.string.studio_verdict_badges_title),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(modifier = Modifier.height(6.dp))
            QualityBadgesRow(badges = scanResult.badges)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Advertencias suaves (Soft Warnings)
        if (hasWarnings) {
            Text(
                text = stringResource(R.string.studio_verdict_warnings_title),
                color = Color(0xFFFFD600),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(modifier = Modifier.height(4.dp))
            scanResult.warnings.forEach { warning ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(text = "•", color = Color(0xFFFFD600), fontSize = 12.sp)
                    Text(
                        text = warning,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Cartel / Disclaimer de aviso suave: "La eleccion esta en ti"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0x26FFD600))
                    .border(1.dp, Color(0x66FFD600), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFFFD600),
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = stringResource(R.string.studio_verdict_soft_warning_disclaimer),
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Sugerencias positivas de impacto
        if (scanResult.suggestions.isNotEmpty()) {
            Text(
                text = stringResource(R.string.studio_verdict_suggestions_title),
                color = Color(0xFF00E5FF),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            )
            Spacer(modifier = Modifier.height(4.dp))
            scanResult.suggestions.forEach { tip ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(text = "•", color = Color(0xFF00E5FF), fontSize = 12.sp)
                    Text(
                        text = tip,
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botones de accion: X (Descartar y volver a disparar) y ✓ (Confirmar/Continuar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Boton X (Descartar / Reintentar)
            OutlinedButton(
                onClick = onRetryClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFFF5252),
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.7f)),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "✕",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5252),
                    )
                    Text(
                        text = stringResource(R.string.studio_verdict_retry),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            // Boton ✓ / OK (Aceptar foto)
            MiraiLinkButton(
                onClick = onAcceptClick,
                modifier = Modifier.weight(1.3f),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "✓",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(
                            if (hasWarnings) R.string.studio_verdict_use_anyway else R.string.studio_verdict_use_photo
                        ),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun HudQualityVerdictSheetPreview() {
    HudQualityVerdictSheet(
        scanResult = MiraiScanResult(
            contentType = com.feryaeljustice.mirailink.domain.model.studio.ScanContentType.FACIAL_PORTRAIT,
            faceBiometrics = null,
            qualityMetrics = ImageQualityMetrics(
                averageLuminancePercent = 65,
                contrastVariance = 0.5f,
                width = 1080,
                height = 1920,
                isLikelyScreenshot = false,
            ),
            badges = listOf(
                com.feryaeljustice.mirailink.domain.model.studio.QualityBadge.OPTIMAL_LIGHTING,
                com.feryaeljustice.mirailink.domain.model.studio.QualityBadge.HIGH_RESOLUTION,
            ),
            warnings = listOf("Small tilt detected"),
            suggestions = listOf("Hold camera steady"),
            canProceedWithSoftWarning = true,
        ),
        onAcceptClick = {},
        onRetryClick = {},
    )
}
