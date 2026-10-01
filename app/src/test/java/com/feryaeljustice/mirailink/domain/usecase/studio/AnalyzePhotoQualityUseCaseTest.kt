package com.feryaeljustice.mirailink.domain.usecase.studio

import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedRect
import com.feryaeljustice.mirailink.domain.model.studio.QualityBadge
import com.feryaeljustice.mirailink.domain.model.studio.ScanContentType
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class AnalyzePhotoQualityUseCaseTest {

    private lateinit var useCase: AnalyzePhotoQualityUseCase

    @Before
    fun setUp() {
        useCase = AnalyzePhotoQualityUseCase()
    }

    @Test
    fun `portrait with flawless face and optimal light awards all facial badges`() {
        val face = FaceBiometrics(
            boundingBox = NormalizedRect(left = 0.35f, top = 0.25f, right = 0.65f, bottom = 0.65f),
            headEulerAngleY = 2f,
            headEulerAngleZ = 1f,
            headEulerAngleX = 0f,
            smilingProbability = 0.85f,
            leftEyeOpenProbability = 0.95f,
            rightEyeOpenProbability = 0.92f,
        )

        val metrics = ImageQualityMetrics(
            averageLuminancePercent = 60,
            contrastVariance = 450f,
            width = 1080,
            height = 1080,
            isLikelyScreenshot = false,
        )

        val result = useCase(metrics, face)

        assertThat(result.contentType).isEqualTo(ScanContentType.FACIAL_PORTRAIT)
        assertThat(result.badges).contains(QualityBadge.OPTIMAL_LIGHTING)
        assertThat(result.badges).contains(QualityBadge.DIRECT_GAZE)
        assertThat(result.badges).contains(QualityBadge.AUTHENTIC_SMILE)
        assertThat(result.badges).contains(QualityBadge.CENTERED_FRAME)
        assertThat(result.badges).contains(QualityBadge.EYES_OPEN)
        assertThat(result.badges).contains(QualityBadge.HIGH_RESOLUTION)
        assertThat(result.warnings).isEmpty()
        assertThat(result.canProceedWithSoftWarning).isTrue()
    }

    @Test
    fun `dark photo with tilted face returns soft warnings without blocking`() {
        val face = FaceBiometrics(
            boundingBox = NormalizedRect(left = 0.05f, top = 0.05f, right = 0.25f, bottom = 0.25f),
            headEulerAngleY = 35f, // Giros pronunciados
            headEulerAngleZ = 20f,
            headEulerAngleX = 25f,
            smilingProbability = 0.1f,
            leftEyeOpenProbability = 0.3f,
            rightEyeOpenProbability = 0.4f,
        )

        val metrics = ImageQualityMetrics(
            averageLuminancePercent = 12, // Muy oscuro
            contrastVariance = 50f,
            width = 720,
            height = 720,
            isLikelyScreenshot = false,
        )

        val result = useCase(metrics, face)

        assertThat(result.contentType).isEqualTo(ScanContentType.FACIAL_PORTRAIT)
        assertThat(result.warnings).isNotEmpty()
        assertThat(result.warnings.any { it.contains("oscur", ignoreCase = true) || it.contains("baja", ignoreCase = true) }).isTrue()
        assertThat(result.canProceedWithSoftWarning).isTrue()
    }

    @Test
    fun `anime artwork or illustration without human face is approved and not rejected`() {
        val metrics = ImageQualityMetrics(
            averageLuminancePercent = 55,
            contrastVariance = 320f,
            width = 1200,
            height = 1200,
            isLikelyScreenshot = false,
        )

        val result = useCase(metrics, faceBiometrics = null)

        assertThat(result.contentType).isEqualTo(ScanContentType.VISUAL_ART_GENERAL)
        assertThat(result.badges).contains(QualityBadge.VISUAL_ART_APPROVED)
        assertThat(result.badges).contains(QualityBadge.OPTIMAL_LIGHTING)
        assertThat(result.badges).contains(QualityBadge.HIGH_RESOLUTION)
        assertThat(result.canProceedWithSoftWarning).isTrue()
    }

    @Test
    fun `screenshot flags warning and provides friendly cropping suggestion`() {
        val metrics = ImageQualityMetrics(
            averageLuminancePercent = 50,
            contrastVariance = 200f,
            width = 1080,
            height = 2400,
            isLikelyScreenshot = true,
            screenshotReason = "Detectada captura de pantalla",
        )

        val result = useCase(metrics, faceBiometrics = null)

        assertThat(result.warnings.any { it.contains("captura de pantalla", ignoreCase = true) }).isTrue()
        assertThat(result.suggestions.any { it.contains("Recorta", ignoreCase = true) }).isTrue()
        assertThat(result.canProceedWithSoftWarning).isTrue()
    }
}
