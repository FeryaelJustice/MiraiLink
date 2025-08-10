package com.feryaeljustice.mirailink.ui.mappers

import com.feryaeljustice.mirailink.domain.model.user.UserPhoto
import com.feryaeljustice.mirailink.ui.viewentries.PhotoSlotViewEntry

fun UserPhoto.toPhotoSlotViewEntry(): PhotoSlotViewEntry = PhotoSlotViewEntry(
    url = url,
    position = position,
)