package com.feryaeljustice.mirailink.ui.screens.studio

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.feryaeljustice.mirailink.data.studio.BitmapOptimizationUtils
import com.feryaeljustice.mirailink.data.studio.FaceDetectorDataSource
import com.feryaeljustice.mirailink.data.studio.QualityMetricsCalculator
import com.feryaeljustice.mirailink.domain.model.studio.ImageQualityMetrics
import com.feryaeljustice.mirailink.domain.usecase.studio.AnalyzePhotoQualityUseCase
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
/** Regression coverage for frozen review, soft warnings and analysis recovery. */
class MiraiStudioViewModelTest {
    @get:Rule val mainCoroutineRule = MainCoroutineRule()
    private val context = mockk<Context>()
    private val uri = mockk<Uri>(relaxed = true)
    private val calculator = mockk<QualityMetricsCalculator>()
    private val detector = mockk<FaceDetectorDataSource>()
    private lateinit var viewModel: MiraiStudioViewModel

    @Before fun setUp() {
        mockkObject(BitmapOptimizationUtils)
        viewModel = MiraiStudioViewModel(AnalyzePhotoQualityUseCase(), calculator, detector, SavedStateHandle())
    }

    @After fun tearDown() {
        unmockkObject(BitmapOptimizationUtils)
    }

    @Test fun `preview freezes before analysis and ignores live frames`() = runTest(mainCoroutineRule.scheduler) {
        val pending = CompletableDeferred<Bitmap?>()
        coEvery { BitmapOptimizationUtils.loadOptimizedBitmap(context, uri) } coAnswers { pending.await() }
        viewModel.analyzeUri(uri, context)
        runCurrent()
        viewModel.onLiveFrameAnalysis(1, null)
        viewModel.confirmSelection()
        assertThat(viewModel.uiState.value.mode).isEqualTo(StudioMode.STATIC_IMAGE_REVIEW)
        assertThat(viewModel.uiState.value.currentImageUri).isEqualTo(uri)
        assertThat(viewModel.uiState.value.liveLuminancePercent).isEqualTo(50)
        assertThat(viewModel.uiState.value.isConfirmed).isFalse()
        pending.complete(null)
        val failed = viewModel.uiState.first { !it.isAnalyzing }
        assertThat(failed.analysisFailed).isTrue()
        viewModel.confirmSelection()
        assertThat(viewModel.uiState.value.isConfirmed).isTrue()
        viewModel.retry()
        assertThat(viewModel.uiState.value.mode).isEqualTo(StudioMode.LIVE_CAMERA)
        assertThat(viewModel.uiState.value.currentImageUri).isNull()
        assertThat(viewModel.uiState.value.analysisFailed).isFalse()
    }

    @Test fun `both quality verdicts remain in preview until explicit confirmation`() = runTest(mainCoroutineRule.scheduler) {
        for (luminance in listOf(0, 50)) {
            val bitmap = mockk<Bitmap>(relaxed = true)
            coEvery { BitmapOptimizationUtils.loadOptimizedBitmap(context, uri) } returns bitmap
            every { calculator.calculateFromBitmap(bitmap, any()) } returns
                ImageQualityMetrics(luminance, 100f, 1000, 1000, false)
            coEvery { detector.detectInBitmap(bitmap) } returns null
            viewModel.analyzeUri(uri, context)
            val reviewed = viewModel.uiState.first { !it.isAnalyzing }
            assertThat(reviewed.mode).isEqualTo(StudioMode.STATIC_IMAGE_REVIEW)
            assertThat(reviewed.scanResult).isNotNull()
            assertThat(reviewed.scanResult!!.warnings.isNotEmpty()).isEqualTo(luminance == 0)
            assertThat(reviewed.isConfirmed).isFalse()
            viewModel.confirmSelection()
            assertThat(viewModel.uiState.value.isConfirmed).isTrue()
            viewModel.retry()
        }
    }
}
