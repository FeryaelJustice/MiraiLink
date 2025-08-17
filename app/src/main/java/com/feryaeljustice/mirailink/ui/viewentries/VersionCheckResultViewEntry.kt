package com.feryaeljustice.mirailink.ui.viewentries

import kotlinx.serialization.Serializable

@Serializable
data class VersionCheckResultViewEntry(
    val mustUpdate: Boolean,
    val shouldUpdate: Boolean,
    val message: String,
    val playStoreUrl: String
)