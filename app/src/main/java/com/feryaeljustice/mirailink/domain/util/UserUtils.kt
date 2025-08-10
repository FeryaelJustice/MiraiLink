package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.ui.viewentries.MinimalUserInfoViewEntry

fun User.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun MinimalUserInfoViewEntry.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}