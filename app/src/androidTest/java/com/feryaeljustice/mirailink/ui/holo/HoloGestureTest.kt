package com.feryaeljustice.mirailink.ui.holo

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.lifecycle.Lifecycle
import coil.Coil
import coil.ImageLoader
import coil.decode.DataSource
import coil.fetch.DrawableResult
import coil.fetch.Fetcher
import coil.request.Options
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.core.holo.*
import com.feryaeljustice.mirailink.domain.model.holo.HoloTilt
import com.feryaeljustice.mirailink.domain.repository.HoloPreferencesRepository
import com.feryaeljustice.mirailink.domain.usecase.haptics.CalculateHeartbeatAffinityUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack
import com.feryaeljustice.mirailink.ui.haptics.HapticHeartbeatController
import com.feryaeljustice.mirailink.ui.screens.settings.components.HoloProfileSetting
import com.feryaeljustice.mirailink.ui.testing.setMiraiLinkContent
import com.feryaeljustice.mirailink.ui.testing.testSession
import com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import io.mockk.mockk
import io.mockk.verify
import io.mockk.every
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Gestos reales de Compose con imagenes locales, sin backend ni sensores fisicos. */
@RunWith(AndroidJUnit4::class)
class HoloGestureTest {
    @get:Rule val rule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var loader: ImageLoader
    private val enabled = MutableStateFlow(true)
    private val prepared = CompletableDeferred<HoloPhotoLayers>()
    private val haptics = mockk<HapticHeartbeatController>(relaxed = true)
    private lateinit var controller: HoloRenderController
    private var likes = 0
    private var passes = 0
    private var undo = 0
    private var heartbeatStarts = 0
    private var closeLabel = ""
    private var firstPhotoLabel = ""
    private var secondPhotoLabel = ""

    @Before fun prepareLocalImages() {
        every { haptics.startHeartbeat(any()) } answers { heartbeatStarts++ }
        val context = rule.activity
        loader = ImageLoader.Builder(context).components {
            add(object : Fetcher.Factory<Uri> {
                override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher? {
                    if (data.host != "holo.test") return null
                    return object : Fetcher {
                        override suspend fun fetch(): DrawableResult {
                            val bitmap = Bitmap.createBitmap(256, 512, Bitmap.Config.ARGB_8888)
                            bitmap.eraseColor(if (data.path?.endsWith("1") == true) Color.MAGENTA else Color.CYAN)
                            return DrawableResult(BitmapDrawable(context.resources, bitmap), false, DataSource.MEMORY)
                        }
                    }
                }
            })
        }.build()
        Coil.setImageLoader(loader)
        controller = HoloRenderController(
            object : HoloPreferencesRepository {
                override fun observeEnabled() = enabled
                override suspend fun setEnabled(enabled: Boolean): MiraiLinkResult<Unit> {
                    this@HoloGestureTest.enabled.value = enabled
                    return MiraiLinkResult.Success(Unit)
                }
            },
            object : HoloMotionSource {
                override val available = true
                override fun observe() = MutableStateFlow(HoloTilt(0.3f, -0.3f))
            },
            object : HoloDevicePolicy { override fun observeMotionAllowed() = MutableStateFlow(true) },
            object : HoloImageProcessor {
                override val generation = MutableStateFlow(0L)
                override suspend fun prepare(bitmap: Bitmap, widthPx: Int, heightPx: Int, density: Float) = prepared.await()
                override fun clear() { generation.value++ }
            },
        )
    }

    @After fun releaseLocalImages() { Coil.reset(); loader.shutdown() }

    /** Conserva los tres botones mientras la segmentacion sigue pendiente. */
    @Test fun buttonsAndUndoWhilePending() {
        showStack()
        rule.onNodeWithTag("likeBtn").performTouchInput { click() }
        rule.waitUntil(5_000) { likes == 1 }
        rule.onNodeWithTag("discardBtn").performClick()
        rule.waitUntil(5_000) { passes == 1 }
        rule.onNodeWithTag("returnSwipeBtn").performClick()
        rule.runOnIdle { assertEquals(1, undo) }
    }

    /** El arrastre conserva el descarte con efecto simple activo. */
    @Test fun swipeWithEffectActive() {
        prepared.complete(HoloPhotoLayers(null))
        showStack()
        rule.waitUntil(5_000) { controller.active.value }
        rule.onNodeWithTag("holo-stack").performTouchInput {
            swipe(Offset(width * 0.9f, height * 0.25f), Offset(width * 0.1f, height * 0.25f), 500)
        }
        rule.waitUntil(5_000) { passes == 1 }
        verify { haptics.stopHeartbeat() }
    }

    /** El ajuste apagado conserva swipe y no registra el sensor simulado. */
    @Test fun swipeWithEffectDisabled() {
        enabled.value = false
        showStack()
        rule.onNodeWithTag("holo-stack").performTouchInput {
            swipe(Offset(width * 0.1f, height * 0.25f), Offset(width * 0.9f, height * 0.25f), 500)
        }
        rule.waitUntil(5_000) { likes == 1 }
        rule.runOnIdle { assertFalse(controller.active.value) }
    }

    /** Toques y pulsacion larga siguen delegando al carrusel y la vista ampliada. */
    @Test fun photoNavigationAndFullscreen() {
        prepared.complete(HoloPhotoLayers(null))
        showStack()
        rule.waitUntil(5_000) { controller.active.value }
        rule.onNodeWithTag("holo-stack").performTouchInput { click(Offset(width * 0.8f, height * 0.2f)) }
        rule.onNodeWithContentDescription(secondPhotoLabel).assertExists()
        rule.onNodeWithTag("holo-stack").performTouchInput { click(Offset(width * 0.2f, height * 0.2f)) }
        rule.onNodeWithContentDescription(firstPhotoLabel).assertExists()
        rule.onNodeWithTag("holo-stack").performTouchInput { longClick(Offset(width * 0.5f, height * 0.2f)) }
        rule.waitUntil(5_000) { !controller.active.value }
        rule.onNodeWithContentDescription(closeLabel).assertIsDisplayed().performClick()
        rule.onNodeWithTag("holo-stack").assertIsDisplayed()
        rule.waitUntil(5_000) { controller.active.value }
    }

    /** Un arrastre cancelado vuelve al reposo sin votar. */
    @Test fun cancelledDragDoesNotVote() {
        showStack()
        rule.onNodeWithTag("holo-stack").performTouchInput {
            down(Offset(width * 0.5f, height * 0.2f))
            moveTo(Offset(width * 0.55f, height * 0.2f))
            cancel()
        }
        rule.runOnIdle { assertEquals(0, likes); assertEquals(0, passes) }
    }

    @Test fun verticalGestureDoesNotVote() {
        prepared.complete(HoloPhotoLayers(null))
        showStack()
        rule.waitUntil(5_000) { controller.active.value }
        rule.onNodeWithTag("holo-stack").performTouchInput {
            swipe(Offset(width * 0.5f, height * 0.7f), Offset(width * 0.5f, height * 0.45f), 500)
        }
        rule.runOnIdle { assertEquals(0, likes); assertEquals(0, passes) }
    }

    @Test fun heartbeatHoldPreservedWhileHoloIsNeutral() {
        prepared.complete(HoloPhotoLayers(null))
        showStack()
        rule.waitUntil(5_000) { controller.active.value }
        rule.onNodeWithTag("likeBtn").performTouchInput { down(center) }
        rule.waitUntil(1_000) { heartbeatStarts > 0 }
        rule.runOnIdle { assertFalse(controller.active.value) }
        rule.onNodeWithTag("likeBtn").performTouchInput { up() }
        rule.runOnIdle { assertEquals(0, likes) }
        verify { haptics.stopHeartbeat() }
    }

    @Test fun backgroundPausesAndResumeRestarts() {
        prepared.complete(HoloPhotoLayers(null))
        showStack()
        rule.waitUntil(5_000) { controller.active.value }
        rule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        rule.waitUntil(5_000) { !controller.active.value }
        rule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        rule.waitUntil(5_000) { controller.active.value }
    }

    @Test fun segmentedLayerIsDecorativeAndClearedInBothThemes() {
        val foreground = Bitmap.createBitmap(128, 256, Bitmap.Config.ARGB_8888).apply { eraseColor(Color.MAGENTA) }
        prepared.complete(HoloPhotoLayers(foreground))
        val dark = mutableStateOf(false)
        showStack(dark)
        rule.waitUntil(5_000) { rule.onAllNodesWithTag("holoPhoto_SegmentedParallax").fetchSemanticsNodes().isNotEmpty() }
        rule.onAllNodesWithContentDescription(firstPhotoLabel).assertCountEquals(1)
        saveFixtureCapture("holo_light")
        rule.runOnIdle { dark.value = true }
        rule.onAllNodesWithContentDescription(firstPhotoLabel).assertCountEquals(1)
        saveFixtureCapture("holo_dark")
        rule.runOnIdle { controller.images.clear() }
        rule.onNodeWithTag("holoPhoto_SegmentedParallax").assertDoesNotExist()
        rule.onNodeWithTag("holoPhoto_SimpleParallax").assertExists()
        // No reciclar el recorte antes de desmontar Compose.
    }

    private fun saveFixtureCapture(name: String) {
        val capture = rule.onNodeWithTag("holo-stack").captureToImage().asAndroidBitmap()
        java.io.File(rule.activity.getExternalFilesDir(null), "$name.png").outputStream().use {
            capture.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    /** El interruptor accesible cambia el valor sin doble accion. */
    @Test fun settingsSwitchDelegatesOnce() {
        var changes = 0
        val checked = mutableStateOf(true)
        rule.setMiraiLinkContent {
            HoloProfileSetting(checked.value, { checked.value = it; changes++ })
        }
        rule.onNode(isToggleable()).assertIsOn().performClick().assertIsOff()
        rule.runOnIdle { assertEquals(1, changes) }
    }

    @Test fun capsuleSwipeKeepsHeartbeatAndHoloSuspended() {
        showStack(veiled = true)
        rule.onNodeWithTag("holo-stack").performTouchInput {
            swipe(Offset(width * 0.1f, height * 0.25f), Offset(width * 0.9f, height * 0.25f), 500)
        }
        rule.waitUntil(5_000) { likes == 1 }
        rule.runOnIdle { assertFalse(controller.active.value) }
        verify { haptics.triggerLikeConfirmation() }
    }

    @Test fun capsulePhotoTapsAndFullscreenKeepTheGlass() {
        showStack(veiled = true)
        rule.onNodeWithTag("holo-stack").performTouchInput { click(Offset(width * 0.8f, height * 0.2f)) }
        rule.onNodeWithContentDescription(secondPhotoLabel).assertExists()
        rule.onNodeWithTag("holo-stack").performTouchInput { longClick(Offset(width * 0.5f, height * 0.2f)) }
        rule.onNodeWithContentDescription(closeLabel).assertIsDisplayed().performClick()
        rule.runOnIdle { assertFalse(controller.active.value) }
    }

    private fun showStack(dark: State<Boolean> = mutableStateOf(false), veiled: Boolean = false) {
        val user = UserViewEntry("local-user", "otaku", "Holo", null, null, "Local profile", null, null,
            listOf(UserPhotoViewEntry("local-user", "https://holo.test/1", 0),
                UserPhotoViewEntry("local-user", "https://holo.test/2", 1)), emptyList(), emptyList()).copy(
                    photoPresentation = if (veiled) com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation("local-capsule") else null)
        val current = mutableStateOf(user)
        val session = testSession()
        val currentUserUseCase = mockk<GetCurrentUserUseCase>()
        val affinity = CalculateHeartbeatAffinityUseCase()
        rule.setContent {
          com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme(dynamicColor = false, darkTheme = dark.value) {
            closeLabel = stringResource(R.string.content_description_user_card_close_btn)
            firstPhotoLabel = stringResource(R.string.content_description_photo_carousel_pager_image, 1)
            secondPhotoLabel = stringResource(R.string.content_description_photo_carousel_pager_image, 2)
            UserSwipeCardStack(
                modifier = Modifier.testTag("holo-stack"), users = listOf(current.value), canUndo = true,
                onSwipeLeft = { passes++; current.value = user.copy(id = "pass-$passes") },
                onGoBack = { undo++ },
                onSwipeRight = { likes++; current.value = user.copy(id = "like-$likes") },
                currentUser = user, hapticController = haptics,
                affinityUseCase = affinity,
                getCurrentUserUseCase = currentUserUseCase,
                miraiLinkSession = session, holoController = controller,
            )
          }
        }
    }
}
