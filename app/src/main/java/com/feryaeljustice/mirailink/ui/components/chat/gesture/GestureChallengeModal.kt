package com.feryaeljustice.mirailink.ui.components.chat.gesture

import androidx.camera.core.CameraSelector
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource
import com.feryaeljustice.mirailink.data.studio.GesturePhotoAnalyzer
import com.feryaeljustice.mirailink.domain.model.chat.gesture.FaceGestureType
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureChallengeSummary
import com.feryaeljustice.mirailink.domain.usecase.chat.gesture.CalculateGestureCompatibilityUseCase
import com.feryaeljustice.mirailink.domain.usecase.chat.gesture.EvaluateFaceGestureUseCase
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.screens.studio.components.CameraPreviewView
import kotlinx.coroutines.delay
import java.util.Locale

private val GESTURE_SEQUENCE = listOf(
    FaceGestureType.WINK_LEFT,
    FaceGestureType.BIG_SMILE,
    FaceGestureType.WINK_RIGHT,
    FaceGestureType.TILT_HEAD,
)

private const val TOTAL_GAME_SECONDS = 15.0f

@Composable
fun GestureChallengeModal(
    onDismiss: () -> Unit,
    onShareResult: (GestureChallengeSummary) -> Unit,
    modifier: Modifier = Modifier,
    faceDetectorDataSource: FaceDetectorDataSource = remember { FaceDetectorDataSource() },
    evaluateGestureUseCase: EvaluateFaceGestureUseCase = remember { EvaluateFaceGestureUseCase() },
    calculateCompatibilityUseCase: CalculateGestureCompatibilityUseCase = remember {
        CalculateGestureCompatibilityUseCase()
    },
) {
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    var currentGestureIndex by remember { mutableIntStateOf(0) }
    var timeRemainingSeconds by remember { mutableFloatStateOf(TOTAL_GAME_SECONDS) }
    var isGameOver by remember { mutableStateOf(false) }
    var resultSummary by remember { mutableStateOf<GestureChallengeSummary?>(null) }
    var showSuccessFlash by remember { mutableStateOf(false) }
    var showInstructionsDialog by remember { mutableStateOf(false) }
    var completedGesturesCount by remember { mutableIntStateOf(0) }

    // Cronometro de 15 segundos
    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            val stepMs = 100L
            while (timeRemainingSeconds > 0f) {
                delay(stepMs)
                timeRemainingSeconds = (timeRemainingSeconds - (stepMs / 1000f)).coerceAtLeast(0f)
                if (timeRemainingSeconds <= 0f) {
                    isGameOver = true
                    val elapsed = TOTAL_GAME_SECONDS - timeRemainingSeconds
                    resultSummary = calculateCompatibilityUseCase(
                        score = completedGesturesCount,
                        totalGestures = GESTURE_SEQUENCE.size,
                        completionTimeSeconds = elapsed,
                    )
                    break
                }
            }
        }
    }

    // Camara Analyzer
    val gestureAnalyzer = remember(currentGestureIndex, isGameOver) {
        GesturePhotoAnalyzer(
            scope = coroutineScope,
            faceDetectorDataSource = faceDetectorDataSource,
            onFaceDetected = { face ->
                if (!isGameOver && currentGestureIndex < GESTURE_SEQUENCE.size) {
                    val targetGesture = GESTURE_SEQUENCE[currentGestureIndex]
                    if (evaluateGestureUseCase(face, targetGesture)) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        showSuccessFlash = true
                        completedGesturesCount++

                        val nextIndex = currentGestureIndex + 1
                        if (nextIndex >= GESTURE_SEQUENCE.size) {
                            // Juego completado con todos los gestos!
                            isGameOver = true
                            val elapsed = TOTAL_GAME_SECONDS - timeRemainingSeconds
                            resultSummary = calculateCompatibilityUseCase(
                                score = completedGesturesCount,
                                totalGestures = GESTURE_SEQUENCE.size,
                                completionTimeSeconds = elapsed,
                            )
                        } else {
                            currentGestureIndex = nextIndex
                        }
                    }
                }
            },
        )
    }

    LaunchedEffect(showSuccessFlash) {
        if (showSuccessFlash) {
            delay(400)
            showSuccessFlash = false
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        ),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                if (resultSummary != null) {
                    GestureResultCard(
                        summary = resultSummary!!,
                        onShareInChat = {
                            onShareResult(resultSummary!!)
                            onDismiss()
                        },
                        onDismiss = onDismiss,
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column {
                                MiraiLinkText(
                                    text = "🎯 " + stringResource(R.string.gesture_roulette_title),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                                MiraiLinkText(
                                    text = stringResource(R.string.gesture_roulette_subtitle),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                MiraiLinkIconButton(onClick = { showInstructionsDialog = true }) {
                                    MiraiLinkText(
                                        text = "ℹ️",
                                        fontSize = 18.sp,
                                    )
                                }
                                MiraiLinkIconButton(onClick = onDismiss) {
                                    MiraiLinkText(
                                        text = "✕",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Pasos (Dots)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            GESTURE_SEQUENCE.forEachIndexed { index, _ ->
                                val isDone = index < currentGestureIndex
                                val isCurrent = index == currentGestureIndex

                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 6.dp)
                                        .size(if (isCurrent) 28.dp else 22.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isDone -> Color(0xFF4CAF50)
                                                isCurrent -> MaterialTheme.colorScheme.primary
                                                else -> MaterialTheme.colorScheme.outlineVariant
                                            },
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    MiraiLinkText(
                                        text = if (isDone) "✓" else "${index + 1}",
                                        color = Color.White,
                                        fontSize = if (isCurrent) 13.sp else 11.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Visor de Camara frontal con overlay
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3f / 4f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center,
                        ) {
                            CameraPreviewView(
                                modifier = Modifier.fillMaxSize(),
                                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
                                analyzer = gestureAnalyzer,
                            )

                            // Flash verde de acierto
                            if (showSuccessFlash) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x664CAF50)),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    MiraiLinkText(
                                        text = stringResource(R.string.gesture_roulette_success_flash),
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                    )
                                }
                            }

                            // Banner inferior con el gesto solicitado
                            if (currentGestureIndex < GESTURE_SEQUENCE.size) {
                                val currentGesture = GESTURE_SEQUENCE[currentGestureIndex]
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.65f))
                                        .padding(vertical = 12.dp, horizontal = 16.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        MiraiLinkText(
                                            text = currentGesture.iconEmoji,
                                            fontSize = 32.sp,
                                        )
                                        MiraiLinkText(
                                            text = stringResource(currentGesture.promptRes),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 17.sp,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Barra de progreso y temporizador
                        val progress = (timeRemainingSeconds / TOTAL_GAME_SECONDS).coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (timeRemainingSeconds <= 4f) Color(0xFFFF5252) else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            MiraiLinkText(
                                text = stringResource(
                                    R.string.gesture_roulette_step_counter,
                                    currentGestureIndex + 1,
                                    GESTURE_SEQUENCE.size,
                                ),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            MiraiLinkText(
                                text = "${String.format(Locale.US, "%.1f", timeRemainingSeconds)}s",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (timeRemainingSeconds <= 4f) Color(0xFFFF5252) else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }

            if (showInstructionsDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showInstructionsDialog = false },
                    title = {
                        MiraiLinkText(
                            text = stringResource(R.string.gesture_roulette_instructions_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    },
                    text = {
                        MiraiLinkText(
                            text = stringResource(R.string.gesture_roulette_instructions_body),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                        )
                    },
                    confirmButton = {
                        com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton(
                            onClick = { showInstructionsDialog = false },
                        ) {
                            MiraiLinkText(text = stringResource(R.string.gesture_roulette_instructions_btn))
                        }
                    },
                )
            }

            // Confetti superpuesto cuando el juego se completa con exito
            if (resultSummary != null) {
                ConfettiCelebration(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
