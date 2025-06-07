package com.feryaeljustice.mirailink.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadPhotoDto(
    @SerialName("position") val position: Int,
    @SerialName("url") val url: String
)