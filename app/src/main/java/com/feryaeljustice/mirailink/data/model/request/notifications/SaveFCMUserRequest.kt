package com.feryaeljustice.mirailink.data.model.request.notifications

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SaveFCMUserRequest(
    @SerialName("fcm") val fcm: String? = "",
)
