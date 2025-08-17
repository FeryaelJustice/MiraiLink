package com.feryaeljustice.mirailink.ui.viewentries.user

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import kotlinx.serialization.Serializable

@Serializable
data class MinimalUserInfoViewEntry(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String = "",
    val gender: String = "",
    val birthdate: String = "",
    val profilePhoto: UserPhoto? = null,
)