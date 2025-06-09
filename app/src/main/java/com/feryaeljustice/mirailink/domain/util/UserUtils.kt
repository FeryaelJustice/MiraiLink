package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.model.user.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.user.User

fun User.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun MinimalUserInfo.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}