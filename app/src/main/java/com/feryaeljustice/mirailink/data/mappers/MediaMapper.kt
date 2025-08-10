package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.UserPhotoDto
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto

fun UserPhotoDto.toDomain(): UserPhoto = UserPhoto(
    userId = userId,
    url = url,
    position = position
)