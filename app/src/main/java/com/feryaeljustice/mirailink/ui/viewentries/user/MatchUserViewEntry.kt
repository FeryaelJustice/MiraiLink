package com.feryaeljustice.mirailink.ui.viewentries.user

data class MatchUserViewEntry(
    val id: String,
    val username: String,
    val nickname: String,
    val avatarUrl: String,
    val isBoosted: Boolean = false
)