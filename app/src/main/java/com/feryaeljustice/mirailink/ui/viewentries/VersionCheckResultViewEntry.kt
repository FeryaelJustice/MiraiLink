package com.feryaeljustice.mirailink.ui.viewentries

data class VersionCheckResultViewEntry(
    val mustUpdate: Boolean,
    val shouldUpdate: Boolean,
    val message: String,
    val playStoreUrl: String
)