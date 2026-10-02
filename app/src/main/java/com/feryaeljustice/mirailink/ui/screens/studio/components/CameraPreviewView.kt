package com.feryaeljustice.mirailink.ui.screens.studio.components

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executors

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    imageCapture: ImageCapture? = null,
    analyzer: ImageAnalysis.Analyzer? = null,
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.fillMaxSize().background(Color.DarkGray))
        return
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    DisposableEffect(cameraSelector, analyzer, lifecycleOwner, imageCapture) {
        val cameraExecutor = Executors.newSingleThreadExecutor()
        var disposed = false
        var boundProvider: ProcessCameraProvider? = null
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val analysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            if (disposed) return@addListener
            try {
                val cameraProvider = cameraProviderFuture.get()
                boundProvider = cameraProvider

                analyzer?.let {
                    analysis.setAnalyzer(cameraExecutor, it)
                }

                cameraProvider.unbindAll()
                val useCases = mutableListOf<androidx.camera.core.UseCase>(preview, analysis)
                if (imageCapture != null) {
                    useCases.add(imageCapture)
                }
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    *useCases.toTypedArray(),
                )
            } catch (_: Exception) {}
        }, ContextCompat.getMainExecutor(context))

        onDispose {
            disposed = true
            analysis.clearAnalyzer()
            try {
                boundProvider?.unbindAll()
            } catch (_: Exception) {}
            cameraExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier.fillMaxSize(),
    )
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun CameraPreviewViewPreview() {
    CameraPreviewView()
}
