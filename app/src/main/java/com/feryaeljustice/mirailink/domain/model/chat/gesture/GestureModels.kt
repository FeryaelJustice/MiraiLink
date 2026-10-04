package com.feryaeljustice.mirailink.domain.model.chat.gesture

import androidx.annotation.StringRes
import com.feryaeljustice.mirailink.R

enum class FaceGestureType(
    @StringRes val promptRes: Int,
    val iconEmoji: String,
) {
    WINK_LEFT(R.string.gesture_roulette_prompt_wink_left, "😉"),
    BIG_SMILE(R.string.gesture_roulette_prompt_big_smile, "😁"),
    WINK_RIGHT(R.string.gesture_roulette_prompt_wink_right, "😜"),
    TILT_HEAD(R.string.gesture_roulette_prompt_tilt_head, "🙃"),
}

data class GestureChallengeSummary(
    val score: Int,
    val totalGestures: Int = 4,
    val completionTimeSeconds: Float,
    val compatibilityPercentage: Int,
    val funTitle: String,
    val description: String,
)

sealed interface GestureMessageParsed {
    data object Regular : GestureMessageParsed
    data object Invite : GestureMessageParsed
    data class Result(val summary: GestureChallengeSummary) : GestureMessageParsed
}

object GestureMessagePayload {
    private const val INVITE_TAG = "[GESTURE_ROULETTE:INVITE]"
    private const val RESULT_PREFIX = "[GESTURE_ROULETTE:RESULT:"
    private const val SUFFIX = "]"

    fun formatInvite(): String = INVITE_TAG

    fun formatResult(summary: GestureChallengeSummary): String {
        return "$RESULT_PREFIX" +
            "score=${summary.score};" +
            "total=${summary.totalGestures};" +
            "time=${String.format(java.util.Locale.US, "%.1f", summary.completionTimeSeconds)};" +
            "match=${summary.compatibilityPercentage};" +
            "title=${summary.funTitle};" +
            "desc=${summary.description}" +
            SUFFIX
    }

    fun parse(text: String): GestureMessageParsed {
        val trimmed = text.trim()
        if (trimmed == INVITE_TAG) {
            return GestureMessageParsed.Invite
        }
        if (trimmed.startsWith(RESULT_PREFIX) && trimmed.endsWith(SUFFIX)) {
            val content = trimmed.removePrefix(RESULT_PREFIX).removeSuffix(SUFFIX)
            val parts = content.split(";").associate { part ->
                val keyValue = part.split("=", limit = 2)
                if (keyValue.size == 2) keyValue[0].trim() to keyValue[1].trim() else "" to ""
            }
            val score = parts["score"]?.toIntOrNull() ?: 0
            val total = parts["total"]?.toIntOrNull() ?: 4
            val time = parts["time"]?.toFloatOrNull() ?: 15.0f
            val match = parts["match"]?.toIntOrNull() ?: 90
            val title = parts["title"]?.takeIf { it.isNotBlank() } ?: "Reflejos de Match"
            val desc = parts["desc"]?.takeIf { it.isNotBlank() } ?: "¡Gran sincronia de gestos!"

            return GestureMessageParsed.Result(
                GestureChallengeSummary(
                    score = score,
                    totalGestures = total,
                    completionTimeSeconds = time,
                    compatibilityPercentage = match,
                    funTitle = title,
                    description = desc,
                )
            )
        }
        return GestureMessageParsed.Regular
    }
}
