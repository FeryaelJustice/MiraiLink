package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.constants.URL_REGEX

fun String.isValidUrl(): Boolean {
    return this.trim().matches(URL_REGEX) && this.isNotBlank()
}

fun String?.getFormattedUrl(): String {
    return if (this == null || !this.isValidUrl()) TEMPORAL_PLACEHOLDER_PICTURE_URL else this
}