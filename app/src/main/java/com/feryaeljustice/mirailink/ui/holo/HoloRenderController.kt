package com.feryaeljustice.mirailink.ui.holo

import com.feryaeljustice.mirailink.core.holo.HoloDevicePolicy
import com.feryaeljustice.mirailink.core.holo.HoloImageProcessor
import com.feryaeljustice.mirailink.core.holo.HoloMotionSource
import com.feryaeljustice.mirailink.domain.model.holo.HoloActivation
import com.feryaeljustice.mirailink.domain.model.holo.HoloTilt
import com.feryaeljustice.mirailink.domain.repository.HoloPreferencesRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

/** Propietario de una pila. No conoce ni emite comandos de swipe o hapticos. */
class HoloRenderController(
    private val preferences: HoloPreferencesRepository,
    private val motion: HoloMotionSource,
    private val policy: HoloDevicePolicy,
    val images: HoloImageProcessor,
) {
    private data class Interaction(val pressed: Boolean = false, val covered: Boolean = false,
        val moving: Boolean = false, val photoKey: String? = null)
    private val interaction = MutableStateFlow(Interaction())
    private val currentTilt = MutableStateFlow(HoloTilt())
    private val running = MutableStateFlow(false)
    private val permitted = MutableStateFlow(false)
    val tilt: StateFlow<HoloTilt> = currentTilt
    val active: StateFlow<Boolean> = running
    val presentationEnabled: StateFlow<Boolean> = permitted

    fun setPressed(pressed: Boolean) { interaction.value = interaction.value.copy(pressed = pressed) }
    fun setMoving(moving: Boolean) { interaction.value = interaction.value.copy(moving = moving) }
    fun setCovered(covered: Boolean) { interaction.value = interaction.value.copy(covered = covered) }
    fun setPhotoReady(key: String) { interaction.value = interaction.value.copy(photoKey = key) }
    fun clearPhoto(key: String) {
        if (interaction.value.photoKey == key) interaction.value = interaction.value.copy(photoKey = null)
    }

    /** La cancelacion de la vinculacion libera callbackFlow y neutraliza el dibujo. */
    suspend fun bind(resumed: Boolean, focused: Boolean, moving: Boolean) {
        try {
            combine(
                preferences.observeEnabled().catch { error ->
                    if (error is CancellationException) throw error
                    emit(false)
                },
                policy.observeMotionAllowed().catch { error ->
                    if (error is CancellationException) throw error
                    emit(false)
                }, interaction,
            ) { enabled, allowed, touch ->
                permitted.value = HoloActivation(enabled, resumed, focused, allowed, motion.available,
                    covered = touch.covered || touch.photoKey == null).active
                HoloActivation(enabled, resumed, focused, allowed, motion.available,
                    interacting = touch.pressed || touch.moving || moving, covered = touch.covered || touch.photoKey == null).active
            }.distinctUntilChanged().collectLatest { enabled ->
                currentTilt.value = HoloTilt()
                running.value = enabled
                if (enabled) {
                    motion.observe().catch { error ->
                        if (error is CancellationException) throw error
                        emit(HoloTilt())
                    }.collect { currentTilt.value = it }
                    running.value = false
                    permitted.value = false
                }
            }
        } finally {
            running.value = false
            permitted.value = false
            currentTilt.value = HoloTilt()
        }
    }
}
