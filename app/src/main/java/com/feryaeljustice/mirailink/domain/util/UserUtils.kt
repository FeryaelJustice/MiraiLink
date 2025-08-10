package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

fun UserViewEntry.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun MinimalUserInfoViewEntry.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}