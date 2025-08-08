package com.feryaeljustice.mirailink.domain.model

data class VersionCheckResult(
    val mustUpdate: Boolean,
    val shouldUpdate: Boolean,
    val message: String,
    val playStoreUrl: String
)