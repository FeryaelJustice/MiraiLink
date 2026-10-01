package com.feryaeljustice.mirailink.domain.model.studio

data class ImageQualityMetrics(
    val averageLuminancePercent: Int, // 0 a 100
    val contrastVariance: Float, // Varianza de luminancia
    val width: Int,
    val height: Int,
    val isLikelyScreenshot: Boolean,
    val screenshotReason: String? = null,
) {
    val isLuminanceOptimal: Boolean
        get() = averageLuminancePercent in 20..90

    val isLuminanceTooDark: Boolean
        get() = averageLuminancePercent < 15

    val isLuminanceTooBright: Boolean
        get() = averageLuminancePercent > 95

    val isResolutionSufficient: Boolean
        get() = width >= 250 && height >= 250

    val luminanceStatus: MetricStatus
        get() = when {
            isLuminanceOptimal -> MetricStatus.EXCELLENT
            averageLuminancePercent in 15..19 || averageLuminancePercent in 91..95 -> MetricStatus.ACCEPTABLE
            else -> MetricStatus.WARNING
        }

    val resolutionStatus: MetricStatus
        get() = when {
            width >= 500 && height >= 500 -> MetricStatus.EXCELLENT
            width >= 250 && height >= 250 -> MetricStatus.ACCEPTABLE
            else -> MetricStatus.WARNING
        }
}
