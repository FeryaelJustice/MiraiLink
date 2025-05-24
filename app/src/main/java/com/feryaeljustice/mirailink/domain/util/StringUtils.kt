package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.constants.URL_REGEX

fun String.isValidUrl(): Boolean {
    return this.trim().matches(URL_REGEX)
}