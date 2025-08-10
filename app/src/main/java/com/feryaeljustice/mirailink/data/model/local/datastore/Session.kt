package com.feryaeljustice.mirailink.data.model.local.datastore

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val token: String = "",
    val userId: String = "",
    val verified: Boolean = false
)