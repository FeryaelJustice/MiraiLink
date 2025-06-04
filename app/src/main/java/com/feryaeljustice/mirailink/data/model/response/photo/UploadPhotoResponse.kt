package com.feryaeljustice.mirailink.data.model.response.photo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadPhotoResponse(
    @SerialName("url") val url: String
)