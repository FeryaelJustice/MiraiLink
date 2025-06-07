package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image_url") val imageUrl: String?
)
