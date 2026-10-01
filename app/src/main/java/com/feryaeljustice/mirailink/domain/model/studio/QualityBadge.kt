package com.feryaeljustice.mirailink.domain.model.studio

import androidx.annotation.StringRes
import com.feryaeljustice.mirailink.R

enum class QualityBadge(
    val id: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
) {
    OPTIMAL_LIGHTING(
        id = "optimal_lighting",
        titleRes = R.string.studio_badge_lighting_title,
        descriptionRes = R.string.studio_badge_lighting_desc,
    ),
    DIRECT_GAZE(
        id = "direct_gaze",
        titleRes = R.string.studio_badge_gaze_title,
        descriptionRes = R.string.studio_badge_gaze_desc,
    ),
    AUTHENTIC_SMILE(
        id = "authentic_smile",
        titleRes = R.string.studio_badge_smile_title,
        descriptionRes = R.string.studio_badge_smile_desc,
    ),
    CENTERED_FRAME(
        id = "centered_frame",
        titleRes = R.string.studio_badge_frame_title,
        descriptionRes = R.string.studio_badge_frame_desc,
    ),
    EYES_OPEN(
        id = "eyes_open",
        titleRes = R.string.studio_badge_eyes_title,
        descriptionRes = R.string.studio_badge_eyes_desc,
    ),
    HIGH_RESOLUTION(
        id = "high_resolution",
        titleRes = R.string.studio_badge_resolution_title,
        descriptionRes = R.string.studio_badge_resolution_desc,
    ),
    VISUAL_ART_APPROVED(
        id = "visual_art_approved",
        titleRes = R.string.studio_badge_art_title,
        descriptionRes = R.string.studio_badge_art_desc,
    ),
}
