package com.feryaeljustice.mirailink.ui.screens.profile.edit

import android.net.Uri
import com.feryaeljustice.mirailink.data.model.response.catalog.ProfileOptionsResponseDto
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

enum class ProfileSingleAttributeType {
    RELIGION,
    ZODIAC_SIGN,
    POLITICAL_STANCE,
    SMOKING_HABIT,
    DRINKING_HABIT,
    SEXUAL_ORIENTATION,
    EDUCATION_LEVEL,
}

enum class ProfileMultiAttributeType {
    RELATIONSHIP_GOALS,
    FAMILY_OPTIONS,
    SPOKEN_LANGUAGES,
}

data class EditProfileUiState(
    val isEditing: Boolean = false,
    val nickname: String = "",
    val bio: String = "",
    val residenceCountryCode: String = "",
    val residenceCountryId: String = "",
    val residenceRegionId: String = "",
    val residenceCityId: String = "",
    val residenceCountryName: String = "",
    val residenceRegion: String = "",
    val residenceCity: String = "",
    val residenceLatitude: Double? = null,
    val residenceLongitude: Double? = null,
    val selectedAnimes: List<AnimeViewEntry> = emptyList(),
    val selectedGames: List<GameViewEntry> = emptyList(),
    val animeCatalog: List<AnimeViewEntry> = emptyList(),
    val gameCatalog: List<GameViewEntry> = emptyList(),
    val profession: String = "",
    val religionId: String? = null,
    val zodiacSignId: String? = null,
    val politicalStanceId: String? = null,
    val smokingHabitId: String? = null,
    val drinkingHabitId: String? = null,
    val sexualOrientationId: String? = null,
    val educationLevelId: String? = null,
    val selectedRelationshipGoalIds: List<String> = emptyList(),
    val selectedFamilyOptionIds: List<String> = emptyList(),
    val selectedSpokenLanguageIds: List<String> = emptyList(),
    val prompts: List<GamerPromptAnswerViewEntry> = emptyList(),
    val profileOptions: ProfileOptionsResponseDto? = null,
    val error: UiError? = null,
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
    data class UpdateResidenceCoordinates(val latitude: Double, val longitude: Double) : EditProfileIntent()
    data class SelectResidencePlace(
        val field: TextFieldType,
        val id: String,
        val name: String,
        val latitude: Double? = null,
        val longitude: Double? = null,
    ) : EditProfileIntent()
    data class EditResidenceText(val field: TextFieldType, val value: String) : EditProfileIntent()
    data class ClearResidenceField(val field: TextFieldType) : EditProfileIntent()
    data class UpdateTags(val field: TagType, val selected: List<String>) : EditProfileIntent()
    data class ReorderPhoto(val from: Int, val to: Int) : EditProfileIntent()
    data class RemovePhoto(val position: Int) : EditProfileIntent()
    data class UpdatePhoto(val position: Int, val uri: Uri) : EditProfileIntent()
    data class OpenPhotoActionDialog(val position: Int) : EditProfileIntent()
    object ClosePhotoDialogs : EditProfileIntent()
    object ShowPhotoSourceDialog : EditProfileIntent()

    // Campos extendidos
    data class UpdateProfession(val value: String) : EditProfileIntent()
    data class SelectSingleAttribute(val type: ProfileSingleAttributeType, val optionId: String?) : EditProfileIntent()
    data class UpdateMultiAttribute(val type: ProfileMultiAttributeType, val selectedIds: List<String>) : EditProfileIntent()
    data class AddOrUpdatePrompt(val promptId: String, val question: String, val answer: String) : EditProfileIntent()
    data class RemovePrompt(val promptId: String) : EditProfileIntent()
    data class ChangePromptQuestion(val oldPromptId: String, val newPromptId: String, val newQuestion: String) : EditProfileIntent()
}

sealed class EditProfileUiEvent {
    object ProfileSavedSuccessfully : EditProfileUiEvent()
}
