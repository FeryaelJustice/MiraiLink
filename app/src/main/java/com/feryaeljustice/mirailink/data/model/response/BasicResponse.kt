package com.feryaeljustice.mirailink.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasicResponse(@SerialName("message") val message: String)
