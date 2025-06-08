package com.feryaeljustice.mirailink.data.model.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckIsVerifiedResponse(@SerialName("isVerified") val isVerified: Boolean)
