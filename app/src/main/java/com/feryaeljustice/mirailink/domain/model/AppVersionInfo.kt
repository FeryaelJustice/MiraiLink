package com.feryaeljustice.mirailink.domain.model

data class AppVersionInfo(
    val platform: String,
    val minVersionCode: Int,
    val latestVersionCode: Int,
    val message: String?,
    val playStoreUrl: String
)