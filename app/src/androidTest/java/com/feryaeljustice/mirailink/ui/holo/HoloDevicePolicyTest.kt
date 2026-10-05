package com.feryaeljustice.mirailink.ui.holo

import android.os.ParcelFileDescriptor
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.feryaeljustice.mirailink.core.holo.AndroidHoloDevicePolicy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import org.junit.Assume.assumeTrue
import org.junit.runner.RunWith

/** Cambia solo opciones del emulador de prueba y restaura sus valores en finally. */
@RunWith(AndroidJUnit4::class)
class HoloDevicePolicyTest {
    private fun shell(command: String): String {
        val descriptor = InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(command)
        return ParcelFileDescriptor.AutoCloseInputStream(descriptor).bufferedReader().use { it.readText().trim() }
    }

    @Test fun disabledAnimationsAndBatterySaverDisableMotion() = runBlocking {
        assumeTrue(android.os.Build.MODEL.contains("sdk_gphone"))
        val policy = AndroidHoloDevicePolicy(ApplicationProvider.getApplicationContext())
        val oldScale = shell("settings get global animator_duration_scale")
        val oldSaver = shell("settings get global low_power")
        try {
            shell("cmd battery unplug")
            shell("cmd battery set level 50")
            shell("cmd power set-mode 0")
            shell("settings put global animator_duration_scale 0")
            assertFalse(withTimeout(5_000) { policy.observeMotionAllowed().first { !it } })
            shell("settings put global animator_duration_scale 1")
            assertTrue(withTimeout(5_000) { policy.observeMotionAllowed().first { it } })
            shell("cmd power set-mode 1")
            assertFalse(withTimeout(5_000) { policy.observeMotionAllowed().first { !it } })
        } finally {
            if (oldScale == "null") shell("settings delete global animator_duration_scale")
            else shell("settings put global animator_duration_scale $oldScale")
            shell("cmd power set-mode ${if (oldSaver == "1") 1 else 0}")
            shell("cmd battery reset")
        }
    }
}
