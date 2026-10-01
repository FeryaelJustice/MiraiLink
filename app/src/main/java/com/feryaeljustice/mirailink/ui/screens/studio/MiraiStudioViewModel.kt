package com.feryaeljustice.mirailink.ui.screens.studio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource
import com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator
import com.feryaeljustice.mirailink.domain.model.studio.FaceBiometrics
import com.feryaeljustice.mirailink.domain.usecase.studio.AnalyzePhotoQualityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MiraiStudioViewModel(
    private val analyzePhotoQualityUseCase: AnalyzePhotoQualityUseCase,
    private val metricsCalculator: QualityMetricsCalculator,
    private val faceDetectorDataSource: FaceDetectorDataSource,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MiraiStudioUiState())
    val uiState: StateFlow<MiraiStudioUiState> = _uiState.asStateFlow()

    fun setTargetSlot(slot: Int?) {
        _uiState.update { it.copy(targetSlot = slot) }
    }

    fun onCameraPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(cameraPermissionGranted = granted) }
    }

    fun switchCamera() {
        if (_uiState.value.mode != StudioMode.LIVE_CAMERA || _uiState.value.isAnalyzing) return
        _uiState.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun onLiveFrameAnalysis(luminancePercent: Int, face: FaceBiometrics?) {
        if (_uiState.value.mode == StudioMode.LIVE_CAMERA && !_uiState.value.isAnalyzing) {
            _uiState.update {
                it.copy(
                    liveLuminancePercent = luminancePercent,
                    liveFaceBiometrics = face,
                )
            }
        }
    }

    fun analyzeUri(uri: Uri, context: Context) {
        _uiState.update {
            it.copy(mode = StudioMode.STATIC_IMAGE_REVIEW, isAnalyzing = true,
                currentImageUri = uri, scanResult = null, analysisFailed = false, isConfirmed = false)
        }
        viewModelScope.launch {
            try {
                val bitmap = loadOptimizedBitmap(context, uri) ?: error("Image unavailable")
                val result = try {
                    withContext(Dispatchers.Default) {
                        val metrics = metricsCalculator.calculateFromBitmap(bitmap, uri.toString())
                        val face = faceDetectorDataSource.detectInBitmap(bitmap)
                        analyzePhotoQualityUseCase(metrics, face)
                    }
                } finally {
                    bitmap.recycle()
                }
                _uiState.update { it.copy(isAnalyzing = false, scanResult = result) }
            } catch (cancelled: kotlinx.coroutines.CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                _uiState.update { it.copy(isAnalyzing = false, analysisFailed = true) }
            }
        }
    }

    fun capturePhoto(imageCapture: ImageCapture, context: Context) {
        if (_uiState.value.mode != StudioMode.LIVE_CAMERA || _uiState.value.isAnalyzing) return
        val photoFile = File(context.cacheDir, "mirai_studio_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        _uiState.update { it.copy(isAnalyzing = true) }

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    analyzeUri(savedUri, context)
                }

                override fun onError(exception: ImageCaptureException) {
                    _uiState.update { it.copy(isAnalyzing = false) }
                }
            },
        )
    }

    fun retry() {
        _uiState.update {
            it.copy(
                mode = StudioMode.LIVE_CAMERA,
                scanResult = null,
                currentImageUri = null,
                isConfirmed = false,
                isAnalyzing = false,
                analysisFailed = false,
            )
        }
    }

    fun confirmSelection() {
        if (_uiState.value.mode != StudioMode.STATIC_IMAGE_REVIEW ||
            _uiState.value.isAnalyzing || _uiState.value.currentImageUri == null) return
        _uiState.update { it.copy(isConfirmed = true) }
    }

    fun showStandardsDialog(show: Boolean) {
        _uiState.update { it.copy(showStandardsDialog = show) }
    }

    private suspend fun loadOptimizedBitmap(context: Context, uri: Uri): Bitmap? =
        com.feryaeljustice.mirailink.data.studio.BitmapOptimizationUtils.loadOptimizedBitmap(context, uri)
}
