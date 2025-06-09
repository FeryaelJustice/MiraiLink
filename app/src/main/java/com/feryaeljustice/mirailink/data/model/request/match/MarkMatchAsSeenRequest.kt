package com.feryaeljustice.mirailink.data.model.request.match

import kotlinx.serialization.SerialName

data class MarkMatchAsSeenRequest(@SerialName("matchIds") val matchIds: List<String>)