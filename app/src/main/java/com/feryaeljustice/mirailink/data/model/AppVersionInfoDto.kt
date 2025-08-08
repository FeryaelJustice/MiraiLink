package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersionInfoDto(
    @SerialName("platform") val platform: String,
    @SerialName("minVersionCode") val minVersionCode: Int,
    @SerialName("latestVersionCode") val latestVersionCode: Int,
    @SerialName("message") val message: String?,
    @SerialName("playStoreUrl") val playStoreUrl: String
)