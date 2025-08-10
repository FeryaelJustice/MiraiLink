package com.feryaeljustice.mirailink.ui.screens.profile.edit

import android.net.Uri
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

data class EditProfileUiState(
    val isEditing: Boolean = false,
    val nickname: String = "",
    val bio: String = "",
    val selectedAnimes: List<AnimeViewEntry> = emptyList(),
    val selectedGames: List<GameViewEntry> = emptyList(),
    val animeCatalog: List<AnimeViewEntry> = emptyList(),
    val gameCatalog: List<GameViewEntry> = emptyList(),
    val photos: List<PhotoSlotViewEntry> = List(4) { PhotoSlotViewEntry() }, // index = position
    val selectedSlotForDialog: Int? = null, // el slot que ha sido clicado
    val showActionDialog: Boolean = false,
    val showPhotoSourceDialog: Boolean = false,
)

sealed class EditProfileIntent {
    // Cada field representa SOLO 1 campo en concreto
    data class Initialize(val user: UserViewEntry) : EditProfileIntent()
    object Save : EditProfileIntent()
    data class UpdateTextField(val field: TextFieldType, val value: String) : EditProfileIntent()
    data class UpdateTags(val field: TagType, val selected: List<String>) : EditProfileIntent()
    data class ReorderPhoto(val from: Int, val to: Int) : EditProfileIntent()
    data class RemovePhoto(val position: Int) : EditProfileIntent()
    data class UpdatePhoto(val position: Int, val uri: Uri) : EditProfileIntent()
    data class OpenPhotoActionDialog(val position: Int) : EditProfileIntent()
    object ClosePhotoDialogs : EditProfileIntent()
    object ShowPhotoSourceDialog : EditProfileIntent()
}

sealed class EditProfileUiEvent {
    object ProfileSavedSuccessfully : EditProfileUiEvent()
    data class ShowError(val message: String) : EditProfileUiEvent()
}