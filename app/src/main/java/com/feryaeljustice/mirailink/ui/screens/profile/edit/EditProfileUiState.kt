package com.feryaeljustice.mirailink.ui.screens.profile.edit

import android.net.Uri
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.ui.viewentities.PhotoSlotViewEntity

data class EditProfileUiState(
    val isEditing: Boolean = false,
    val nickname: String = "",
    val bio: String = "",
    val selectedAnimes: List<String> = emptyList(),
    val selectedGames: List<String> = emptyList(),
    val availableAnimes: List<String> = emptyList(),
    val availableGames: List<String> = emptyList(),
    val photos: List<PhotoSlotViewEntity> = List(4) { PhotoSlotViewEntity() }, // index = position
    val selectedSlotForDialog: Int? = null, // el slot que ha sido clicado
    val showActionDialog: Boolean = false,
    val showPhotoSourceDialog: Boolean = false
)

sealed class EditProfileIntent {
    // Cada field representa SOLO 1 campo en concreto
    data class Initialize(val user: User) : EditProfileIntent()
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