package com.feryaeljustice.mirailink.domain.constants

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
 * Starts with http://10.0.2.2:3000 inside emulator before the /assets...
 * in production domain remove http://10.0.2.2:3000 and replace with https://mirailink.xyz
 */
const val TEMPORAL_PLACEHOLDER_PICTURE_URL =
    "https://mirailink.xyz/assets/img/profiles/Goku.jpeg"

const val deepLinkBaseUrl = "https://mirailink.xyz"
const val deepLinkPrivacyPolicyUrl = "$deepLinkBaseUrl/privacypolicy"