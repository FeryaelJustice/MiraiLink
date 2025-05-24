package com.feryaeljustice.mirailink.data.model.request

import kotlinx.serialization.SerialName

data class MarkMatchAsSeenRequest(@SerialName("matchIds") val matchIds: List<String>)
