package com.feryaeljustice.mirailink.data.model.request.report

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportUserRequest(
    @SerialName("reportedUser") val reportedUser: String,
    @SerialName("reason") val reason: String
)
