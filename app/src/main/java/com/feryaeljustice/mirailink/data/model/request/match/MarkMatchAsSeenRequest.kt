package com.feryaeljustice.mirailink.data.model.request.match

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarkMatchAsSeenRequest(
    @SerialName("matchIds") val matchIds: List<String>,
)
