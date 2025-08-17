package com.feryaeljustice.mirailink.ui.viewentries.media

import kotlinx.serialization.Serializable

@Serializable
data class UserPhotoViewEntry(
    val userId: String,
    val url: String,
    val position: Int
)