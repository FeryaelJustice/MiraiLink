package com.feryaeljustice.mirailink.ui.viewentries

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto

data class MinimalUserInfoViewEntry(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String = "",
    val gender: String = "",
    val birthdate: String = "",
    val profilePhoto: UserPhoto? = null,
)