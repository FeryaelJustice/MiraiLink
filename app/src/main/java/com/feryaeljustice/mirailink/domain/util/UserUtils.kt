package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.model.MinimalUserInfo
import com.feryaeljustice.mirailink.domain.model.User

fun User.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun MinimalUserInfo.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}