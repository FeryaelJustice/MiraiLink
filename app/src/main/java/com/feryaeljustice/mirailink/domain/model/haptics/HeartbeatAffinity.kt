package com.feryaeljustice.mirailink.domain.model.haptics

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class HeartbeatAffinity(
    val ratio: Float,
    val percentage: Int,
    val bpm: Int,
    val commonAnimesCount: Int,
    val commonGamesCount: Int,
    val commonGoalsCount: Int,
) {
    companion object {
        val DefaultNeutral =
            HeartbeatAffinity(
                ratio = 0.20f,
                percentage = 20,
                bpm = 71,
                commonAnimesCount = 0,
                commonGamesCount = 0,
                commonGoalsCount = 0,
            )
    }
}
