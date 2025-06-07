package com.feryaeljustice.mirailink.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserPhoto(
    val id: String,
    val userId: String,
    val url: String,
    val position: Int
)
