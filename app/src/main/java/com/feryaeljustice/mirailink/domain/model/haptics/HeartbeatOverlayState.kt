package com.feryaeljustice.mirailink.domain.model.haptics

import androidx.compose.runtime.Immutable

@Immutable
data class HeartbeatOverlayState(
    val visible: Boolean = false,
    val affinity: HeartbeatAffinity = HeartbeatAffinity.DefaultNeutral,
    val progress: Float = 0f,
    val targetNickname: String = "",
    val isSwipe: Boolean = false,
)
