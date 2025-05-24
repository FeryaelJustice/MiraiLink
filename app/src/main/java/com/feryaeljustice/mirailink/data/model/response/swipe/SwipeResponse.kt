package com.feryaeljustice.mirailink.data.model.response.swipe

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SwipeResponse(
    @SerialName("message") val message: String,
    @SerialName("match") val match: Boolean
)