package com.feryaeljustice.mirailink.data.studio

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StudioPhotoAnalyzer(
    private val scope: CoroutineScope,
    private val faceDetectorDataSource: FaceDetectorDataSource,
    private val metricsCalculator: QualityMetricsCalculator,
    private val onAnalysisResult: (luminancePercent: Int, face: FaceBiometrics?) -> Unit,
) : ImageAnalysis.Analyzer {

    @Volatile
    private var isProcessing = false

    @OptIn(ExperimentalGetImage::class)
    /**
     * Consume un frame CameraX; descarta frames si ya hay análisis o no existe mediaImage.
     * La coroutine cierra ImageProxy en finally y libera la guarda tras ML Kit. El callback admite rostro null.
     */
    override fun analyze(imageProxy: ImageProxy) {
        if (isProcessing) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        isProcessing = true
        val rotationDegrees = imageProxy.imageInfo.rotationDegrees

        // Calcular luminancia desde el buffer Y
        val yPlane = mediaImage.planes[0]
        val luminance = metricsCalculator.calculateLuminanceFromYPlane(
            yBuffer = yPlane.buffer,
            width = imageProxy.width,
            height = imageProxy.height,
            pixelStride = yPlane.pixelStride,
            rowStride = yPlane.rowStride,
        )

        scope.launch {
            try {
                val face = faceDetectorDataSource.detectInMediaImage(mediaImage, rotationDegrees)
                onAnalysisResult(luminance, face)
            } catch (_: Exception) {
                onAnalysisResult(luminance, null)
            } finally {
                imageProxy.close()
                isProcessing = false
            }
        }
    }
}
