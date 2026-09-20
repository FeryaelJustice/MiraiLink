package com.feryaeljustice.mirailink.domain.constants

import com.feryaeljustice.mirailink.BuildConfig

const val TIME_24_HOURS = 86_400_000
const val HTTP_REGEX_STRING = "^(http|https)://.*"
val HTTP_REGEX = HTTP_REGEX_STRING.toRegex()

val URL_REGEX = Regex(
    pattern = """^(https?://|www\.)\S+(\.\S+)+$""",
    options = setOf(RegexOption.IGNORE_CASE)
)

/**
 * TEMPORAL_PLACEHOLDER_PICTURE_URL.
 * This is the temporal URL for the placeholder picture.
 * Keep this aligned with the backend base URL used by NetworkModule in the
 * current development build. Coil falls back to the local logo if it fails.
 */
val TEMPORAL_PLACEHOLDER_PICTURE_URL =
    "${BuildConfig.MIRAILINK_BASE_URL}/assets/img/profiles/Goku.webp"

const val deepLinkBaseUrl = "https://mirailink.xyz"
const val deepLinkPrivacyPolicyUrl = "$deepLinkBaseUrl/privacypolicy"
