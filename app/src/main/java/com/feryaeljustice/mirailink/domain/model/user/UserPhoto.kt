package com.feryaeljustice.mirailink.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserPhoto(
    val userId: String,
    val url: String,
    val position: Int
)
