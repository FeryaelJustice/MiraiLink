package com.feryaeljustice.mirailink.data.model.request.feedback

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendFeedbackRequest(
    @SerialName("feedback") val feedback: String
)
