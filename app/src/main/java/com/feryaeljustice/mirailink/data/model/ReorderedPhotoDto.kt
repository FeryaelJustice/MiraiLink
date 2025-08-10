package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReorderedPhotoDto(
    @SerialName("url") val url: String,
    @SerialName("position") val position: Int
)