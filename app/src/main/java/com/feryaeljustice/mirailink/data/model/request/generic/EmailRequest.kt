package com.feryaeljustice.mirailink.data.model.request.generic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(@SerialName("email") val email: String)