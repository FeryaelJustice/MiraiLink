package com.feryaeljustice.mirailink.ui.screens.studio

import android.net.Uri
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult

enum class StudioMode {
    LIVE_CAMERA,
    STATIC_IMAGE_REVIEW,
}

data class MiraiStudioUiState(
    val mode: StudioMode = StudioMode.LIVE_CAMERA,
    val isFrontCamera: Boolean = true,
    val liveLuminancePercent: Int = 50,
    val liveFaceBiometrics: FaceBiometrics? = null,
    val currentImageUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val analysisFailed: Boolean = false,
    val scanResult: MiraiScanResult? = null,
    val targetSlot: Int? = null,
    val isConfirmed: Boolean = false,
    val showStandardsDialog: Boolean = false,
    val cameraPermissionGranted: Boolean = false,
)
