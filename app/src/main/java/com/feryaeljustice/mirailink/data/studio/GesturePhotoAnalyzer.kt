package com.feryaeljustice.mirailink.data.studio

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GesturePhotoAnalyzer(
    private val scope: CoroutineScope,
    private val faceDetectorDataSource: FaceDetectorDataSource,
    private val onFaceDetected: (FaceBiometrics?) -> Unit,
) : ImageAnalysis.Analyzer {

    @Volatile
    private var isProcessing = false

    @OptIn(ExperimentalGetImage::class)
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

        scope.launch {
            try {
                val face = faceDetectorDataSource.detectInMediaImage(mediaImage, rotationDegrees)
                onFaceDetected(face)
            } catch (_: Exception) {
                onFaceDetected(null)
            } finally {
                imageProxy.close()
                isProcessing = false
            }
        }
    }
}
