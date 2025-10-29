package com.feryaeljustice.mirailink.data.mappers.ui

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry

fun UserPhotoViewEntry.toPhotoSlotViewEntry(): PhotoSlotViewEntry = PhotoSlotViewEntry(
    url = url,
    position = position,
)

fun UserPhoto.toUserPhotoViewEntry(): UserPhotoViewEntry = UserPhotoViewEntry(
    userId = userId,
    url = url,
    position = position
)