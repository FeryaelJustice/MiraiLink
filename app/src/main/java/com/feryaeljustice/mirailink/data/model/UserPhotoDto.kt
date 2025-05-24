package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserPhotoDto(
    @SerialName("id")
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("url")
    val url: String,
    @SerialName("position")
    val position: Int
)