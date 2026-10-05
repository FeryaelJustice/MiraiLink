package com.feryaeljustice.mirailink.ui.holo

import com.feryaeljustice.mirailink.core.holo.HoloDevicePolicy
import com.feryaeljustice.mirailink.core.holo.HoloImageProcessor
import com.feryaeljustice.mirailink.core.holo.HoloMotionSource
import com.feryaeljustice.mirailink.domain.model.holo.HoloTilt
import com.feryaeljustice.mirailink.domain.repository.HoloPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/** Registro, pausa y cancelacion con fuentes locales deterministas. */
@OptIn(ExperimentalCoroutinesApi::class)
class HoloRenderControllerTest {
    @Test fun `foto ajuste y politica controlan un solo listener`() = runTest {
        val fixture = Fixture()
        val job = backgroundScope.launch { fixture.controller.bind(true, true, false) }
        runCurrent()
        assertEquals(0, fixture.listeners)
        fixture.controller.setPhotoReady("photo-a")
        runCurrent()
        assertEquals(1, fixture.listeners)
        fixture.enabled.value = false
        runCurrent()
        assertEquals(0, fixture.listeners)
        assertFalse(fixture.controller.active.value)
        fixture.enabled.value = true
        runCurrent()
        fixture.allowed.value = false
        runCurrent()
        assertEquals(0, fixture.listeners)
        job.cancel()
    }

    @Test fun `pulsacion y modal neutralizan sin perder foto`() = runTest {
        val fixture = Fixture()
        fixture.controller.setPhotoReady("photo-a")
        backgroundScope.launch { fixture.controller.bind(true, true, false) }
        runCurrent()
        fixture.motion.value = HoloTilt(0.5f, -0.5f)
        runCurrent()
        assertEquals(fixture.motion.value, fixture.controller.tilt.value)
        fixture.controller.setPressed(true)
        runCurrent()
        assertEquals(0, fixture.listeners)
        assertEquals(HoloTilt(), fixture.controller.tilt.value)
        assertTrue(fixture.controller.presentationEnabled.value)
        fixture.controller.setPressed(false)
        runCurrent()
        assertEquals(1, fixture.listeners)
        fixture.controller.setMoving(true)
        runCurrent()
        assertEquals(0, fixture.listeners)
        assertTrue(fixture.controller.presentationEnabled.value)
        fixture.controller.setMoving(false)
        runCurrent()
        assertEquals(1, fixture.listeners)
        fixture.controller.setCovered(true)
        runCurrent()
        assertEquals(0, fixture.listeners)
    }

    @Test fun `foto anterior no invalida reemplazo y cancelacion libera`() = runTest {
        val fixture = Fixture()
        fixture.controller.setPhotoReady("photo-a")
        val job = backgroundScope.launch { fixture.controller.bind(true, true, false) }
        runCurrent()
        fixture.controller.setPhotoReady("photo-b")
        fixture.controller.clearPhoto("photo-a")
        runCurrent()
        assertEquals(1, fixture.listeners)
        job.cancel()
        runCurrent()
        assertEquals(0, fixture.listeners)
        assertEquals(HoloTilt(), fixture.controller.tilt.value)
        assertFalse(fixture.controller.active.value)
    }

    @Test fun `pantalla oculta desenfocada o animando no registra`() = runTest {
        for ((resumed, focused, moving) in listOf(Triple(false, true, false),
            Triple(true, false, false), Triple(true, true, true))) {
            val fixture = Fixture()
            fixture.controller.setPhotoReady("photo")
            val job = backgroundScope.launch { fixture.controller.bind(resumed, focused, moving) }
            runCurrent()
            assertEquals(0, fixture.listeners)
            job.cancel()
        }
    }

    private class Fixture {
        val enabled = MutableStateFlow(true)
        val allowed = MutableStateFlow(true)
        val motion = MutableStateFlow(HoloTilt())
        var listeners = 0
        val controller = HoloRenderController(
            object : HoloPreferencesRepository {
                override fun observeEnabled() = enabled
                override suspend fun setEnabled(enabled: Boolean): MiraiLinkResult<Unit> {
                    this@Fixture.enabled.value = enabled
                    return MiraiLinkResult.Success(Unit)
                }
            },
            object : HoloMotionSource {
                override val available = true
                override fun observe() = flow {
                    listeners++
                    try { emitAll(motion) } finally { listeners-- }
                }
            },
            object : HoloDevicePolicy { override fun observeMotionAllowed() = allowed },
            mockk<HoloImageProcessor>(),
        )
    }
}
