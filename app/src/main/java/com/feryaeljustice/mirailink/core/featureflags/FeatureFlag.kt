package com.feryaeljustice.mirailink.core.featureflags

data class FeatureFlag(
    val key: String,
    val enabled: Boolean,
)

const val FLAG_ENABLE_CHRISTMAS_THEME = "enable_christmas_theme"
