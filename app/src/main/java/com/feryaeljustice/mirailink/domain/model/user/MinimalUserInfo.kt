package com.feryaeljustice.mirailink.domain.model.user

import kotlinx.serialization.Serializable

@Serializable
data class MinimalUserInfo(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String = "",
    val gender: String = "",
    val birthdate: String = "",
    val profilePhoto: UserPhoto? = null,
)