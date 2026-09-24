package com.feryaeljustice.mirailink.domain.model.swipe

import com.feryaeljustice.mirailink.domain.model.user.User

data class ReceivedLike(
    val likeId: String,
    val likedAt: String,
    val user: User,
)
