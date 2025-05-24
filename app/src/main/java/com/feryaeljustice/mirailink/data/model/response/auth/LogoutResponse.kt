package com.feryaeljustice.mirailink.data.model.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(@SerialName("message") val message: String)
