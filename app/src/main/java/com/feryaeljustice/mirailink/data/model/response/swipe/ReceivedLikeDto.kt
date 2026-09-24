package com.feryaeljustice.mirailink.data.model.response.swipe

import com.feryaeljustice.mirailink.data.model.UserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedLikeDto(
    @SerialName("likeId")
    val likeId: String,
    @SerialName("likedAt")
    val likedAt: String,
    @SerialName("user")
    val user: UserDto,
)
