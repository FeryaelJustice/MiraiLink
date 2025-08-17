package com.feryaeljustice.mirailink.ui.viewentries.user

import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.media.UserPhotoViewEntry
import kotlinx.serialization.Serializable

@Serializable
data class UserViewEntry(
    val id: String,
    val username: String,
    val nickname: String,
    val email: String?,
    val phoneNumber: String?,
    val bio: String?,
    val gender: String?, // "male", "female", "non_binary", "other", "prefer_not_to_say"
    val birthdate: String?, // "YYYY-MM-DD"
    val photos: List<UserPhotoViewEntry> = emptyList(),
    val games: List<GameViewEntry>,
    val animes: List<AnimeViewEntry>,
)