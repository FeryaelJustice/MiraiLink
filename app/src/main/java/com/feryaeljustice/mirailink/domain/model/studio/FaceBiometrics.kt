package com.feryaeljustice.mirailink.domain.model.studio

data class NormalizedPoint(
    val x: Float,
    val y: Float,
)

data class NormalizedRect(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
) {
    val width: Float get() = (right - left).coerceAtLeast(0f)
    val height: Float get() = (bottom - top).coerceAtLeast(0f)
    val centerX: Float get() = (left + right) / 2f
    val centerY: Float get() = (top + bottom) / 2f
}

data class FaceBiometrics(
    val boundingBox: NormalizedRect,
    val headEulerAngleY: Float, // Yaw: giro horizontal (negativo: izquierda, positivo: derecha)
    val headEulerAngleZ: Float, // Roll: inclinacion de cabeza
    val headEulerAngleX: Float, // Pitch: inclinacion vertical (arriba/abajo)
    val smilingProbability: Float?, // 0.0 a 1.0
    val leftEyeOpenProbability: Float?, // 0.0 a 1.0
    val rightEyeOpenProbability: Float?, // 0.0 a 1.0
    val leftEyePosition: NormalizedPoint? = null,
    val rightEyePosition: NormalizedPoint? = null,
    val noseBasePosition: NormalizedPoint? = null,
    val mouthCenterPosition: NormalizedPoint? = null,
) {
    val isFacingDirectly: Boolean
        get() = kotlin.math.abs(headEulerAngleY) <= 30f && kotlin.math.abs(headEulerAngleZ) <= 25f && kotlin.math.abs(headEulerAngleX) <= 30f

    val isSmiling: Boolean
        get() = (smilingProbability ?: 0f) >= 0.40f

    val areBothEyesOpen: Boolean
        get() = (leftEyeOpenProbability ?: 0f) >= 0.35f && (rightEyeOpenProbability ?: 0f) >= 0.35f

    val isCenteredInThirds: Boolean
        get() {
            return boundingBox.centerX in 0.15f..0.85f && boundingBox.centerY in 0.15f..0.85f
        }
}
