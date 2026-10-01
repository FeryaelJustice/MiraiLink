package com.feryaeljustice.mirailink.domain.usecase.studio

import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics
import com.feryaeljustice.mirailink.domain.model.studio.MetricStatus
import com.feryaeljustice.mirailink.domain.model.studio.MiraiScanResult
import com.feryaeljustice.mirailink.domain.model.studio.QualityBadge
import com.feryaeljustice.mirailink.domain.model.studio.ScanContentType

class AnalyzePhotoQualityUseCase {

    operator fun invoke(
        qualityMetrics: ImageQualityMetrics,
        faceBiometrics: FaceBiometrics?,
    ): MiraiScanResult {
        val badges = mutableListOf<QualityBadge>()
        val warnings = mutableListOf<String>()
        val suggestions = mutableListOf<String>()

        val contentType = if (faceBiometrics != null) {
            ScanContentType.FACIAL_PORTRAIT
        } else {
            ScanContentType.VISUAL_ART_GENERAL
        }

        // 1. Evaluacion de iluminacion comun
        if (qualityMetrics.isLuminanceOptimal) {
            badges.add(QualityBadge.OPTIMAL_LIGHTING)
        } else if (qualityMetrics.isLuminanceTooDark) {
            warnings.add("Iluminacion muy baja. Intenta encender una luz o colocarte frente a una ventana.")
            suggestions.add("Una luz frontal suave mejora la claridad y los detalles de tu perfil.")
        } else if (qualityMetrics.isLuminanceTooBright) {
            warnings.add("Foto sobreexpuesta o con exceso de brillo.")
            suggestions.add("Evita contraluces intensos directos hacia el lente.")
        }

        // 2. Evaluacion de resolucion comun
        if (qualityMetrics.resolutionStatus == MetricStatus.EXCELLENT) {
            badges.add(QualityBadge.HIGH_RESOLUTION)
        } else if (!qualityMetrics.isResolutionSufficient) {
            warnings.add("Resolucion baja (${qualityMetrics.width}x${qualityMetrics.height} px). Podria verse pixelada.")
            suggestions.add("Usa fotos con al menos 720p para que luzcan nitidas en cualquier pantalla.")
        }

        // 3. Deteccion de captura de pantalla
        if (qualityMetrics.isLikelyScreenshot) {
            warnings.add("Posible captura de pantalla detectada.")
            suggestions.add("Recorta la imagen para eliminar barras de bateria y notificaciones, o sube el archivo original.")
        }

        // 4. Evaluacion especifica por tipo
        if (faceBiometrics != null) {
            if (faceBiometrics.isFacingDirectly) {
                badges.add(QualityBadge.DIRECT_GAZE)
            } else {
                warnings.add("Rostro inclinado o mirando hacia un lado.")
                suggestions.add("Mirar directo al frente genera un 40% mas de afinidad visual.")
            }

            if (faceBiometrics.isSmiling) {
                badges.add(QualityBadge.AUTHENTIC_SMILE)
            }

            if (faceBiometrics.isCenteredInThirds) {
                badges.add(QualityBadge.CENTERED_FRAME)
            } else {
                warnings.add("Rostro fuera de los puntos focales centrales.")
                suggestions.add("Centra tus ojos en el tercio superior para una composicion fotografica ideal.")
            }

            if (faceBiometrics.areBothEyesOpen) {
                badges.add(QualityBadge.EYES_OPEN)
            }
        } else {
            // Arte, anime, cosplay de cuerpo entero o paisaje
            if (qualityMetrics.isResolutionSufficient && !qualityMetrics.isLuminanceTooDark) {
                badges.add(QualityBadge.VISUAL_ART_APPROVED)
                suggestions.add("Composicion visual aprobada. Muestra tus gustos e identidad visual con claridad.")
            }
        }

        return MiraiScanResult(
            contentType = contentType,
            faceBiometrics = faceBiometrics,
            qualityMetrics = qualityMetrics,
            badges = badges,
            warnings = warnings,
            suggestions = suggestions,
            canProceedWithSoftWarning = true,
        )
    }
}
