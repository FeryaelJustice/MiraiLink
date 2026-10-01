package com.feryaeljustice.mirailink.ui.screens.studio

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource
import com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator
import com.feryaeljustice.mirailink.data.studio.StudioPhotoAnalyzer
import com.feryaeljustice.mirailink.domain.model.studio.MetricStatus
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.screens.studio.components.CameraPreviewView
import com.feryaeljustice.mirailink.ui.screens.studio.components.FaceBoxOverlay
import com.feryaeljustice.mirailink.ui.screens.studio.components.HudMetricGauge
import com.feryaeljustice.mirailink.ui.screens.studio.components.HudQualityVerdictSheet
import com.feryaeljustice.mirailink.ui.screens.studio.components.LaserScanOverlay
import com.feryaeljustice.mirailink.ui.screens.studio.components.RuleOfThirdsOverlay
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import android.widget.Toast
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MiraiStudioScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    onBackClick: () -> Unit,
    onPhotoConfirmed: (Uri, Int?) -> Unit,
    onNavigateToFaq: () -> Unit,
    modifier: Modifier = Modifier,
    targetSlot: Int? = null,
    initialUri: Uri? = null,
    viewModel: MiraiStudioViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    val captureDescription = stringResource(R.string.studio_hud_capture_button)

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        viewModel.onCameraPermissionResult(granted)
    }

    LaunchedEffect(Unit) {
        miraiLinkSession.hideBars()
        viewModel.setTargetSlot(targetSlot)
        val hasCamPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.onCameraPermissionResult(hasCamPermission)

        if (initialUri != null) {
            viewModel.analyzeUri(initialUri, context)
        }
    }

    LaunchedEffect(uiState.isConfirmed, uiState.currentImageUri) {
        if (uiState.isConfirmed && uiState.currentImageUri != null) {
            onPhotoConfirmed(uiState.currentImageUri!!, uiState.targetSlot)
        }
    }

    val cameraSelector = if (uiState.isFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    val analyzer = remember(uiState.mode) {
        StudioPhotoAnalyzer(
            scope = coroutineScope,
            faceDetectorDataSource = FaceDetectorDataSource(),
            metricsCalculator = QualityMetricsCalculator(),
            onAnalysisResult = { lum, face ->
                viewModel.onLiveFrameAnalysis(lum, face)
            },
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF030712))
            .then(
                if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                } else {
                    Modifier
                },
            ),
    ) {
        // Vista Principal (Camara en vivo o Revision de imagen)
        if (uiState.mode == StudioMode.LIVE_CAMERA) {
            if (uiState.cameraPermissionGranted) {
                CameraPreviewView(
                    cameraSelector = cameraSelector,
                    imageCapture = imageCapture,
                    analyzer = analyzer,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                // Estado de solicitud de permiso
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(64.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.studio_camera_permission_required),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    MiraiLinkButton(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
                    ) {
                        Text(
                            text = stringResource(R.string.studio_camera_permission_grant),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                        )
                    }

                }
            }

            // Capa de graficos HUD en vivo
            RuleOfThirdsOverlay()
            LaserScanOverlay(isScanning = true)
            FaceBoxOverlay(
                faceBiometrics = uiState.liveFaceBiometrics,
                isFrontCamera = uiState.isFrontCamera,
            )
        } else {
            // Modo Revision Estatica
            uiState.currentImageUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                RuleOfThirdsOverlay()
                LaserScanOverlay(isScanning = false)
                FaceBoxOverlay(
                    faceBiometrics = uiState.scanResult?.faceBiometrics,
                    isFrontCamera = false,
                )
            }
        }

        // Barra Superior HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkIconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xB3000000)),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.White,
                )
            }

            // Badge central sci-fi
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xCC080D1A))
                    .border(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                val statusText = if (uiState.mode == StudioMode.LIVE_CAMERA) {
                    if (uiState.liveFaceBiometrics != null) {
                        stringResource(R.string.studio_hud_face_detected)
                    } else {
                        stringResource(R.string.studio_hud_scanning)
                    }
                } else {
                    if (uiState.scanResult?.faceBiometrics != null) {
                        stringResource(R.string.studio_hud_face_detected)
                    } else {
                        stringResource(R.string.studio_hud_visual_art_mode)
                    }
                }

                Text(
                    text = statusText,
                    color = Color(0xFF00E5FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                )
            }

            // Boton de alternar camara (solo en vivo)
            if (uiState.mode == StudioMode.LIVE_CAMERA && uiState.cameraPermissionGranted) {
                MiraiLinkIconButton(
                    onClick = { viewModel.switchCamera() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xB3000000)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.studio_hud_switch_camera),
                        tint = Color.White,
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // Medidor de Luz Flotante en Vivo
        if (uiState.mode == StudioMode.LIVE_CAMERA && uiState.cameraPermissionGranted) {
            val lum = uiState.liveLuminancePercent
            val status = when {
                lum in 20..90 -> MetricStatus.EXCELLENT
                lum in 15..19 || lum in 91..95 -> MetricStatus.ACCEPTABLE
                else -> MetricStatus.WARNING
            }

            HudMetricGauge(
                label = stringResource(R.string.studio_hud_luminance_label),
                valuePercent = lum,
                status = status,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(top = 64.dp, start = 16.dp)
                    .width(130.dp),
            )

            // Chip informativo de diagnóstico en vivo en tiempo real (Rostro, Iluminación, Simetría)
            val liveFace = uiState.liveFaceBiometrics
            val (guideText, guideColor) = when {
                lum < 20 -> "ILUMINACIÓN BAJA - MÁS LUZ RECOMENDADA" to Color(0xFFFFD600)
                lum > 95 -> "LUZ MUY ALTA - EVITA CONTRALUZ" to Color(0xFFFFD600)
                liveFace == null -> "BUSCANDO ROSTRO / MODO ARTE LIBRE" to Color(0xFF00E5FF)
                !liveFace.isFacingDirectly -> "MIRA DIRECTO AL FRENTE" to Color(0xFFFFD600)
                !liveFace.isCenteredInThirds -> "CENTRA EL ENCUADRE" to Color(0xFFFFD600)
                liveFace.isSmiling -> "¡SONRISA DETECTADA! EXCELENTE" to Color(0xFF00E676)
                else -> "ENFOCADO Y LISTO PARA CAPTURAR" to Color(0xFF00E676)
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xCC080D1A))
                    .border(1.dp, guideColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
            ) {
                Text(
                    text = guideText,
                    color = guideColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }

        // Cargando Analisis
        if (uiState.isAnalyzing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = Color(0xFF00E5FF))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ANALIZANDO BIOMETRÍA Y CALIDAD...",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
        }

        // Barra Inferior de Controles (Modo en vivo)
        if (uiState.mode == StudioMode.LIVE_CAMERA) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Boton de Disparo Neon
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .semantics { contentDescription = captureDescription }
                        .clip(CircleShape)
                        .background(Color(0x3300E5FF))
                        .border(2.dp, Color(0xFF00E5FF), CircleShape)
                        .clickable(enabled = uiState.cameraPermissionGranted && !uiState.isAnalyzing) {
                            viewModel.capturePhoto(imageCapture, context)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                    )
                }

            }
        }

        if (uiState.analysisFailed && uiState.mode == StudioMode.STATIC_IMAGE_REVIEW) {
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
                    .fillMaxWidth().background(Color(0xF00A0F1E)).padding(20.dp),
            ) {
                Text(stringResource(R.string.studio_analysis_failed), color = Color.White)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiraiLinkButton(onClick = { viewModel.retry() }, modifier = Modifier.weight(1f)) {
                        Text("✕ " + stringResource(R.string.studio_verdict_retry))
                    }
                    MiraiLinkButton(onClick = { viewModel.confirmSelection() }, modifier = Modifier.weight(1f)) {
                        Text("✓ " + stringResource(R.string.studio_verdict_use_anyway))
                    }
                }
            }
        }

        // Hoja de Veredicto HUD al congelar foto
        if (uiState.mode == StudioMode.STATIC_IMAGE_REVIEW && uiState.scanResult != null && !uiState.isAnalyzing) {
            HudQualityVerdictSheet(
                scanResult = uiState.scanResult!!,
                onAcceptClick = { viewModel.confirmSelection() },
                onRetryClick = { viewModel.retry() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding(),
            )
        }
    }
}
