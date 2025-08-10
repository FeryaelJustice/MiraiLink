package com.feryaeljustice.mirailink.ui.viewentries.user

import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry

data class UserViewEntry(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String?,
    val phoneNumber: String?,
    val bio: String?,
    val gender: String?,
    val birthdate: String?,
    val photos: List<UserPhotoViewEntry> = emptyList(),
    val games: List<GameViewEntry>,
    val animes: List<AnimeViewEntry>,
)