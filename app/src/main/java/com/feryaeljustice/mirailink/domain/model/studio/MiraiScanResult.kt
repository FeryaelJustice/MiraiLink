package com.feryaeljustice.mirailink.domain.model.studio

enum class ScanContentType {
    FACIAL_PORTRAIT,
    VISUAL_ART_GENERAL,
}

data class MiraiScanResult(
    val contentType: ScanContentType,
    val faceBiometrics: FaceBiometrics?,
    val qualityMetrics: ImageQualityMetrics,
    val badges: List<QualityBadge>,
    val warnings: List<String>,
    val suggestions: List<String>,
    val canProceedWithSoftWarning: Boolean = true,
) {
    val isFlawless: Boolean
        get() = warnings.isEmpty() && badges.isNotEmpty()
}
