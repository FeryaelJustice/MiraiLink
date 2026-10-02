package com.feryaeljustice.mirailink.ui.utils.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender

@Composable
fun Gender.localizedLabel(): String {
    return when (this) {
        Gender.Male -> stringResource(R.string.gender_male)
        Gender.Female -> stringResource(R.string.gender_female)
        Gender.Other -> stringResource(R.string.gender_other)
    }
}

/**
 * Safely resolves a country name for a given country code without throwing
 * [java.util.IllformedLocaleException] if the code is invalid or malformed.
 */
fun String.toCountryNameOrNull(displayLocale: java.util.Locale = java.util.Locale.getDefault()): String? {
    val trimmed = this.trim()
    if (trimmed.isEmpty()) return null
    return runCatching {
        java.util.Locale.Builder().setRegion(trimmed).build().getDisplayCountry(displayLocale).takeIf { it.isNotBlank() }
    }.getOrNull()
}