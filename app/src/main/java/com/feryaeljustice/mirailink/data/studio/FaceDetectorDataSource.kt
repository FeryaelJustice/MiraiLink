package com.feryaeljustice.mirailink.data.studio

import android.graphics.Bitmap
import android.media.Image
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedPoint
import com.feryaeljustice.mirailink.domain.model.studio.NormalizedRect
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class FaceDetectorDataSource {

    private val liveDetector: FaceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .build()
        FaceDetection.getClient(options)
    }

    private val accurateDetector: FaceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.10f)
            .build()
        FaceDetection.getClient(options)
    }

    suspend fun detectInBitmap(bitmap: Bitmap): FaceBiometrics? {
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val faces = accurateDetector.process(inputImage).awaitTask()
        val primaryFace = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() } ?: return null
        return mapToFaceBiometrics(primaryFace, bitmap.width, bitmap.height)
    }

    suspend fun detectInMediaImage(image: Image, rotationDegrees: Int): FaceBiometrics? {
        val inputImage = InputImage.fromMediaImage(image, rotationDegrees)
        val faces = liveDetector.process(inputImage).awaitTask()
        val primaryFace = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() } ?: return null
        val (w, h) = if (rotationDegrees == 90 || rotationDegrees == 270) {
            image.height to image.width
        } else {
            image.width to image.height
        }
        return mapToFaceBiometrics(primaryFace, w, h)
    }

    fun mapToFaceBiometrics(face: Face, imageWidth: Int, imageHeight: Int): FaceBiometrics {
        val w = imageWidth.toFloat().coerceAtLeast(1f)
        val h = imageHeight.toFloat().coerceAtLeast(1f)

        val box = face.boundingBox
        val normRect = NormalizedRect(
            left = (box.left / w).coerceIn(0f, 1f),
            top = (box.top / h).coerceIn(0f, 1f),
            right = (box.right / w).coerceIn(0f, 1f),
            bottom = (box.bottom / h).coerceIn(0f, 1f),
        )

        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position?.let {
            NormalizedPoint(it.x / w, it.y / h)
        }
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position?.let {
            NormalizedPoint(it.x / w, it.y / h)
        }
        val nose = face.getLandmark(FaceLandmark.NOSE_BASE)?.position?.let {
            NormalizedPoint(it.x / w, it.y / h)
        }
        val mouth = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position?.let {
            NormalizedPoint(it.x / w, it.y / h)
        }

        return FaceBiometrics(
            boundingBox = normRect,
            headEulerAngleY = face.headEulerAngleY,
            headEulerAngleZ = face.headEulerAngleZ,
            headEulerAngleX = face.headEulerAngleX,
            smilingProbability = face.smilingProbability,
            leftEyeOpenProbability = face.leftEyeOpenProbability,
            rightEyeOpenProbability = face.rightEyeOpenProbability,
            leftEyePosition = leftEye,
            rightEyePosition = rightEye,
            noseBasePosition = nose,
            mouthCenterPosition = mouth,
        )
    }

    private suspend fun <T> Task<T>.awaitTask(): T =
        suspendCancellableCoroutine { cont ->
            addOnSuccessListener { cont.resumeWith(Result.success(it)) }
            addOnFailureListener { cont.resumeWithException(it) }
            addOnCanceledListener { cont.cancel() }
        }
}
