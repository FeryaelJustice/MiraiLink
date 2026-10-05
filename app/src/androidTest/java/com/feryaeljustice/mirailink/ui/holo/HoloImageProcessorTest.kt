package com.feryaeljustice.mirailink.ui.holo

import android.graphics.Bitmap
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.core.holo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/** Bitmaps reales, motor controlado: sin perfiles personales ni peticiones al backend. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class HoloImageProcessorTest {
    private val context = ApplicationProvider.getApplicationContext<android.content.Context>()
    private fun bitmap(color: Int = Color.MAGENTA, width: Int = 64) =
        Bitmap.createBitmap(width, 64, Bitmap.Config.ARGB_8888).apply { eraseColor(color) }
    private fun emptyMask() = HoloMask(100, 100, FloatArray(10_000))
    private fun squareMask() = HoloMask(100, 100, FloatArray(10_000) {
        if (it % 100 in 25..74 && it / 100 in 25..74) 1f else 0f
    })

    @Test fun cacheUsesContentSizeAndGeneration() = runTest {
        var calls = 0
        val processor = MlKitHoloImageProcessor(context, StandardTestDispatcher(testScheduler)) {
            calls++; squareMask()
        }
        val source = bitmap()
        try {
            val first = processor.prepare(source, 4096, 4096, 1f)
            assertNotNull(first.foreground)
            assertSame(first, processor.prepare(source, 4096, 4096, 1f))
            assertEquals(1, calls)
            processor.prepare(source, 4095, 4096, 1f)
            assertEquals(2, calls)
            source.eraseColor(Color.CYAN)
            processor.prepare(source, 4096, 4096, 1f)
            assertEquals(3, calls)
            processor.clear()
            assertEquals(1L, processor.generation.value)
            processor.prepare(source, 4096, 4096, 1f)
            assertEquals(4, calls)
            assertFalse(source.isRecycled)
        } finally { processor.clear(); context.unregisterComponentCallbacks(processor); source.recycle() }
    }

    @Test fun cancelledProcessingKeepsMutexAndDropsObsoleteCache() = runTest {
        val gate = CompletableDeferred<Unit>()
        var calls = 0
        var running = 0
        var peak = 0
        val processor = MlKitHoloImageProcessor(context, StandardTestDispatcher(testScheduler)) {
            calls++; running++; peak = maxOf(peak, running)
            try { if (calls == 1) gate.await(); emptyMask() } finally { running-- }
        }
        val source = bitmap()
        try {
            val obsolete = launch { processor.prepare(source, 400, 800, 1f) }
            runCurrent()
            obsolete.cancel()
            processor.clear()
            val abandoned = launch { processor.prepare(source, 400, 800, 1f) }
            runCurrent()
            abandoned.cancel()
            val latest = async { processor.prepare(source, 400, 800, 1f) }
            runCurrent()
            assertEquals(1, calls)
            gate.complete(Unit)
            advanceUntilIdle()
            latest.await(); obsolete.join(); abandoned.join()
            assertEquals(2, calls)
            assertEquals(1, peak)
            assertEquals(0, running)
            assertFalse(source.isRecycled)
        } finally { processor.clear(); context.unregisterComponentCallbacks(processor); source.recycle() }
    }

    @Test fun failuresAndMemoryPressureUseFallbackAndReleaseCache() = runTest {
        var calls = 0
        val processor = MlKitHoloImageProcessor(context, StandardTestDispatcher(testScheduler)) {
            calls++
            if (calls == 1) error("controlled failure")
            squareMask()
        }
        val source = bitmap()
        try {
            assertNull(processor.prepare(source, 4096, 4096, 1f).foreground)
            val prepared = processor.prepare(source, 4096, 4096, 1f)
            assertNotNull(prepared.foreground)
            @Suppress("DEPRECATION")
            processor.onTrimMemory(android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW)
            assertEquals(1L, processor.generation.value)
            assertNotSame(prepared, processor.prepare(source, 4096, 4096, 1f))
            assertEquals(3, calls)
        } finally { processor.clear(); context.unregisterComponentCallbacks(processor); source.recycle() }
    }

    @Test fun inputIsSoftwareBoundedAndOwnedCopyIsReleased() = runTest {
        var received: Bitmap? = null
        val processor = MlKitHoloImageProcessor(context, StandardTestDispatcher(testScheduler)) {
            received = it
            assertTrue(it.width <= 1024 && it.height <= 1024)
            assertEquals(Bitmap.Config.ARGB_8888, it.config)
            emptyMask()
        }
        val source = bitmap(width = 2048)
        try {
            assertNull(processor.prepare(source, 400, 800, 1f).foreground)
            assertTrue(received!!.isRecycled)
            assertFalse(source.isRecycled)
        } finally { processor.clear(); context.unregisterComponentCallbacks(processor); source.recycle() }
    }

    @Test fun bundledSdkProcessesLocalBitmap() = runBlocking {
        val processor = MlKitHoloImageProcessor(context, Dispatchers.Default)
        val source = bitmap()
        try {
            withTimeout(30_000) { processor.prepare(source, 400, 800, 1f) }
            assertFalse(source.isRecycled)
        } finally { processor.clear(); context.unregisterComponentCallbacks(processor); source.recycle() }
    }
}
