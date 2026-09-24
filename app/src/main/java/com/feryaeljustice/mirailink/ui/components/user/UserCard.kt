package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto
import com.feryaeljustice.mirailink.ui.components.catalog.ProfileMultiOptionPickerModal
import com.feryaeljustice.mirailink.ui.components.catalog.ProfileSingleOptionPickerModal
import com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileMultiAttributeType
import com.feryaeljustice.mirailink.ui.screens.profile.edit.ProfileSingleAttributeType
import com.feryaeljustice.mirailink.ui.components.user.GamerPromptCard
import com.feryaeljustice.mirailink.ui.components.user.GamerPromptEditSection
import com.feryaeljustice.mirailink.ui.components.user.ChipFlowRow
import com.feryaeljustice.mirailink.ui.components.user.buildPersonalChips
import com.feryaeljustice.mirailink.ui.components.user.buildCategorizedPersonalInfo
import com.feryaeljustice.mirailink.ui.components.user.CategorizedPersonalInfoSection
import androidx.compose.material3.Surface
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.enum.Gender
import com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.domain.util.toAgeOrNull
import com.feryaeljustice.mirailink.domain.util.toBackendDate
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.catalog.InterestsGrid
import com.feryaeljustice.mirailink.ui.components.catalog.VisualInterestPickerModal
import com.feryaeljustice.mirailink.ui.components.catalog.toInterestItemData
import com.feryaeljustice.mirailink.ui.components.media.EditablePhotoGrid
import com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview
import com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel
import com.feryaeljustice.mirailink.ui.components.molecules.BirthdateField
import com.feryaeljustice.mirailink.ui.components.molecules.GenderSelector
import com.feryaeljustice.mirailink.ui.components.molecules.ResidenceSelector
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.utils.extensions.localizedLabel
import com.feryaeljustice.mirailink.ui.utils.extensions.shadow
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun UserCard(
    user: UserViewEntry,
    onSave: (() -> Unit),
    modifier: Modifier = Modifier,
    isPreviewMode: Boolean = false,
    editUiState: EditProfileUiState? = null,
    onValueChange: ((field: TextFieldType, value: String) -> Unit)? = null,
    onResidenceCoordinatesSelected: ((latitude: Double, longitude: Double) -> Unit)? = null,
    onResidencePlaceSelected: ((TextFieldType, GeographicPlace) -> Unit)? = null,
    onResidenceTextChanged: ((TextFieldType, String) -> Unit)? = null,
    onResidenceFieldCleared: ((TextFieldType) -> Unit)? = null,
    onTagSelect: ((type: TagType, newValue: List<String>) -> Unit)? = null,
    onPhotoSlotClick: ((Int) -> Unit)? = null,
    onPhotoReorder: ((from: Int, to: Int) -> Unit)? = null,
    onEdit: ((Boolean) -> Unit)? = null,
    isPublicPresentation: Boolean = false,
    onProfessionChange: ((String) -> Unit)? = null,
    onSingleAttributeSelect: ((ProfileSingleAttributeType, String?) -> Unit)? = null,
    onMultiAttributeUpdate: ((ProfileMultiAttributeType, List<String>) -> Unit)? = null,
    onAddOrUpdatePrompt: ((promptId: String, question: String, answer: String) -> Unit)? = null,
    onRemovePrompt: ((promptId: String) -> Unit)? = null,
    onChangePromptQuestion: ((oldPromptId: String, newPromptId: String, newQuestion: String) -> Unit)? = null,
) {
    val (fullscreenImageUrl, setFullscreenImageUrl) = remember { mutableStateOf<String?>(null) }

    if (fullscreenImageUrl != null) {
        FullscreenImagePreview(
            imageUrl = fullscreenImageUrl,
            onDismiss = { setFullscreenImageUrl(null) },
            closeContentDescription = stringResource(R.string.content_description_user_card_close_btn),
            imageContentDescription = stringResource(R.string.content_description_user_card_fullscreen_img),
        )
    }

    if (isPublicPresentation) {
        PublicUserCard(
            user = user,
            onLongPressOnImage = setFullscreenImageUrl,
            modifier = modifier,
        )
        return
    }

    var showAnimePicker by remember { mutableStateOf(false) }
    var showGamePicker by remember { mutableStateOf(false) }
    var activeSinglePicker by remember { mutableStateOf<ProfileSingleAttributeType?>(null) }
    var activeMultiPicker by remember { mutableStateOf<ProfileMultiAttributeType?>(null) }

    if (showAnimePicker && editUiState != null) {
        VisualInterestPickerModal(
            title = stringResource(R.string.user_card_fav_animes),
            options = editUiState.animeCatalog.map { it.toInterestItemData() },
            selectedIds = editUiState.selectedAnimes.map { it.id },
            onSelectionChange = { selectedIds ->
                onTagSelect?.invoke(TagType.ANIME, selectedIds)
            },
            onDismiss = { showAnimePicker = false },
        )
    }

    if (showGamePicker && editUiState != null) {
        VisualInterestPickerModal(
            title = stringResource(R.string.user_card_fav_games),
            options = editUiState.gameCatalog.map { it.toInterestItemData() },
            selectedIds = editUiState.selectedGames.map { it.id },
            onSelectionChange = { selectedIds ->
                onTagSelect?.invoke(TagType.GAME, selectedIds)
            },
            onDismiss = { showGamePicker = false },
        )
    }

    activeSinglePicker?.let { singleType ->
        if (editUiState != null) {
            val options = editUiState.profileOptions
            val titleRes = when (singleType) {
                ProfileSingleAttributeType.EDUCATION_LEVEL -> R.string.profile_education_label
                ProfileSingleAttributeType.SMOKING_HABIT -> R.string.profile_smoking_label
                ProfileSingleAttributeType.DRINKING_HABIT -> R.string.profile_drinking_label
                ProfileSingleAttributeType.ZODIAC_SIGN -> R.string.profile_zodiac_label
                ProfileSingleAttributeType.RELIGION -> R.string.profile_religion_label
                ProfileSingleAttributeType.POLITICAL_STANCE -> R.string.profile_political_label
                ProfileSingleAttributeType.SEXUAL_ORIENTATION -> R.string.profile_sexual_orientation_label
            }
            val items = when (singleType) {
                ProfileSingleAttributeType.EDUCATION_LEVEL -> options?.educationLevels ?: emptyList()
                ProfileSingleAttributeType.SMOKING_HABIT -> options?.smokingHabits ?: emptyList()
                ProfileSingleAttributeType.DRINKING_HABIT -> options?.drinkingHabits ?: emptyList()
                ProfileSingleAttributeType.ZODIAC_SIGN -> options?.zodiacSigns ?: emptyList()
                ProfileSingleAttributeType.RELIGION -> options?.religions ?: emptyList()
                ProfileSingleAttributeType.POLITICAL_STANCE -> options?.politicalStances ?: emptyList()
                ProfileSingleAttributeType.SEXUAL_ORIENTATION -> options?.sexualOrientations ?: emptyList()
            }
            val selectedId = when (singleType) {
                ProfileSingleAttributeType.EDUCATION_LEVEL -> editUiState.educationLevelId
                ProfileSingleAttributeType.SMOKING_HABIT -> editUiState.smokingHabitId
                ProfileSingleAttributeType.DRINKING_HABIT -> editUiState.drinkingHabitId
                ProfileSingleAttributeType.ZODIAC_SIGN -> editUiState.zodiacSignId
                ProfileSingleAttributeType.RELIGION -> editUiState.religionId
                ProfileSingleAttributeType.POLITICAL_STANCE -> editUiState.politicalStanceId
                ProfileSingleAttributeType.SEXUAL_ORIENTATION -> editUiState.sexualOrientationId
            }

            ProfileSingleOptionPickerModal(
                title = stringResource(titleRes),
                options = items,
                selectedId = selectedId,
                onSelect = { optionId ->
                    onSingleAttributeSelect?.invoke(singleType, optionId)
                },
                onDismiss = { activeSinglePicker = null },
            )
        }
    }

    activeMultiPicker?.let { multiType ->
        if (editUiState != null) {
            val options = editUiState.profileOptions
            val titleRes = when (multiType) {
                ProfileMultiAttributeType.RELATIONSHIP_GOALS -> R.string.profile_relationship_goals_label
                ProfileMultiAttributeType.FAMILY_OPTIONS -> R.string.profile_family_options_label
                ProfileMultiAttributeType.SPOKEN_LANGUAGES -> R.string.profile_languages_label
            }
            val items = when (multiType) {
                ProfileMultiAttributeType.RELATIONSHIP_GOALS -> options?.relationshipGoals ?: emptyList()
                ProfileMultiAttributeType.FAMILY_OPTIONS -> options?.familyOptions ?: emptyList()
                ProfileMultiAttributeType.SPOKEN_LANGUAGES -> options?.spokenLanguages ?: emptyList()
            }
            val selectedIds = when (multiType) {
                ProfileMultiAttributeType.RELATIONSHIP_GOALS -> editUiState.selectedRelationshipGoalIds
                ProfileMultiAttributeType.FAMILY_OPTIONS -> editUiState.selectedFamilyOptionIds
                ProfileMultiAttributeType.SPOKEN_LANGUAGES -> editUiState.selectedSpokenLanguageIds
            }

            ProfileMultiOptionPickerModal(
                title = stringResource(titleRes),
                options = items,
                selectedIds = selectedIds,
                onConfirm = { newSelectedIds ->
                    onMultiAttributeUpdate?.invoke(multiType, newSelectedIds)
                },
                onDismiss = { activeMultiPicker = null },
                showSearch = multiType == ProfileMultiAttributeType.SPOKEN_LANGUAGES,
            )
        }
    }

    val (focusRequester) = FocusRequester.createRefs()

    Card(
        modifier =
            modifier
                .fillMaxSize()
                .shadow(
                    color = MaterialTheme.colorScheme.onSurface,
                    alpha = 0.5f,
                    offsetX = (4).dp,
                    offsetY = (4).dp,
                    blurRadius = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                ).testTag("userCard"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(width = 2.dp, MaterialTheme.colorScheme.onSurface),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (editUiState?.isEditing == true) {
                MiraiLinkOutlinedIconButton(
                    modifier =
                        Modifier
                            .padding(20.dp)
                            .align(Alignment.TopEnd)
                            .alpha(0.8f)
                            .zIndex(10f),
                    // Lo eleva sobre el grid
                    colors =
                        IconButtonDefaults.outlinedIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        ),
                    onClick = { onEdit?.invoke(false) },
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.content_description_user_card_close_edit_mode),
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .then(
                            if (editUiState != null && editUiState.isEditing) Modifier.padding(16.dp) else Modifier,
                        ).verticalScroll(rememberScrollState()),
            ) {
                if (editUiState != null && editUiState.isEditing) {
                    MiraiLinkText(
                        text = stringResource(R.string.profile_section_basic),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    // Cuadrícula de imágenes
                    EditablePhotoGrid(
                        photos = editUiState.photos,
                        onSlotClick = onPhotoSlotClick,
                        onPhotoReorder = onPhotoReorder,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // TextField para nombre
                    MiraiLinkOutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        value = editUiState.nickname,
                        onValueChange = { onValueChange?.invoke(TextFieldType.NICKNAME, it) },
                        label = stringResource(id = R.string.user_card_nickname),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions =
                            KeyboardActions(
                                onNext = {
                                    focusRequester.requestFocus()
                                },
                            ),
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // TextField para bio
                    MiraiLinkOutlinedTextField(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester),
                        value = editUiState.bio,
                        onValueChange = { onValueChange?.invoke(TextFieldType.BIO, it) },
                        label = stringResource(id = R.string.user_card_bio),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    MiraiLinkText(
                        text = stringResource(R.string.profile_section_residence),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ResidenceSelector(
                        countryId = editUiState.residenceCountryId,
                        country = editUiState.residenceCountryName,
                        regionId = editUiState.residenceRegionId,
                        region = editUiState.residenceRegion,
                        city = editUiState.residenceCity,
                        onPlaceSelected = { field, place -> onResidencePlaceSelected?.invoke(field, place) },
                        onTextChanged = { field, value -> onResidenceTextChanged?.invoke(field, value) },
                        onClear = { field -> onResidenceFieldCleared?.invoke(field) },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_work_edu),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MiraiLinkOutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = editUiState.profession,
                        onValueChange = { onProfessionChange?.invoke(it) },
                        label = stringResource(R.string.profile_profession_label),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_education_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.educationLevels, editUiState.educationLevelId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.EDUCATION_LEVEL },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_prompts),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_prompts_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    GamerPromptEditSection(
                        prompts = editUiState.prompts,
                        availableCatalogPrompts = editUiState.profileOptions?.prompts ?: emptyList(),
                        onAddOrUpdatePrompt = { pId, q, a -> onAddOrUpdatePrompt?.invoke(pId, q, a) },
                        onChangePromptQuestion = { oldId, newId, newQ -> onChangePromptQuestion?.invoke(oldId, newId, newQ) },
                        onRemovePrompt = { pId -> onRemovePrompt?.invoke(pId) },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_goals),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_relationship_goals_label),
                        currentValue = getMultiOptionLabels(editUiState.profileOptions?.relationshipGoals, editUiState.selectedRelationshipGoalIds),
                        onClick = { activeMultiPicker = ProfileMultiAttributeType.RELATIONSHIP_GOALS },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_family_options_label),
                        currentValue = getMultiOptionLabels(editUiState.profileOptions?.familyOptions, editUiState.selectedFamilyOptionIds),
                        onClick = { activeMultiPicker = ProfileMultiAttributeType.FAMILY_OPTIONS },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_lifestyle),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_smoking_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.smokingHabits, editUiState.smokingHabitId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.SMOKING_HABIT },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_drinking_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.drinkingHabits, editUiState.drinkingHabitId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.DRINKING_HABIT },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_identity),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_zodiac_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.zodiacSigns, editUiState.zodiacSignId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.ZODIAC_SIGN },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_religion_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.religions, editUiState.religionId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.RELIGION },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_political_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.politicalStances, editUiState.politicalStanceId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.POLITICAL_STANCE },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_sexual_orientation_label),
                        currentValue = getSingleOptionLabel(editUiState.profileOptions?.sexualOrientations, editUiState.sexualOrientationId),
                        onClick = { activeSinglePicker = ProfileSingleAttributeType.SEXUAL_ORIENTATION },
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.profile_edit_section_languages),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProfileAttributeSelectRow(
                        label = stringResource(R.string.profile_languages_label),
                        currentValue = getMultiOptionLabels(editUiState.profileOptions?.spokenLanguages, editUiState.selectedSpokenLanguageIds),
                        onClick = { activeMultiPicker = ProfileMultiAttributeType.SPOKEN_LANGUAGES },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MiraiLinkText(
                        text = stringResource(R.string.profile_section_interests),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Animes favoritos (Edición visual)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_animes),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        MiraiLinkButton(
                            onClick = { showAnimePicker = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            MiraiLinkText(text = stringResource(R.string.interest_picker_manage_animes))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InterestsGrid(
                        items = editUiState.selectedAnimes.map { it.toInterestItemData() },
                        emptyText = stringResource(id = R.string.user_card_fav_animes_empty),
                        onRemoveItem = { itemToRemove ->
                            val updated = editUiState.selectedAnimes.filter { it.id != itemToRemove.id }.map { it.id }
                            onTagSelect?.invoke(TagType.ANIME, updated)
                        },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Videojuegos favoritos (Edición visual)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall,
                        )
                        MiraiLinkButton(
                            onClick = { showGamePicker = true },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            MiraiLinkText(text = stringResource(R.string.interest_picker_manage_games))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    InterestsGrid(
                        items = editUiState.selectedGames.map { it.toInterestItemData() },
                        emptyText = stringResource(id = R.string.user_card_fav_games_empty),
                        onRemoveItem = { itemToRemove ->
                            val updated = editUiState.selectedGames.filter { it.id != itemToRemove.id }.map { it.id }
                            onTagSelect?.invoke(TagType.GAME, updated)
                        },
                    )

                    Spacer(modifier = Modifier.height(64.dp))
                } else {
                    PhotoCarousel(
                        photoUrls = user.photos.map { it.url },
                        onLongPressOnImage = { url ->
                            setFullscreenImageUrl(url)
                        },
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        MiraiLinkText(
                            text = user.nicknameElseUsername(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.Underline),
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileReadOnlySectionHeader(
                            title = stringResource(R.string.profile_section_basic),
                            icon = Icons.Default.Info,
                        )

                        // Ubicacion y distancia geografica
                        val locationParts = mutableListOf<String>()
                        if (!user.residenceCity.isNullOrBlank()) {
                            val countryName = user.residenceCountryCode?.let { code ->
                                java.util.Locale("", code).getDisplayCountry(java.util.Locale.getDefault())
                            }
                            val place = listOfNotNull(user.residenceCity, countryName).joinToString(", ")
                            locationParts.add(stringResource(R.string.card_lives_in, place))
                        }
                        val formattedDist = com.feryaeljustice.mirailink.domain.util.GeoUtils.formatDistance(user.distanceKm)
                        if (formattedDist != null) {
                            locationParts.add(formattedDist)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        MiraiLinkText(
                            text = stringResource(R.string.profile_bio_label),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        MiraiLinkText(
                            text = if (!user.bio.isNullOrBlank()) user.bio else stringResource(id = R.string.bio_not_set),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic,
                        )

                        Gender.fromRealValue(user.gender)?.let { genderEnum ->
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkText(
                                text =
                                    stringResource(
                                        R.string.gender_presentation,
                                        genderEnum.localizedLabel(),
                                    ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic,
                            )
                        } ?: run {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkText(
                                text = stringResource(R.string.profile_gender_not_set),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic,
                            )
                        }

                        val age = user.birthdate.toAgeOrNull()
                        if (!age.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkText(
                                text = stringResource(R.string.age_presentation, age),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic,
                            )
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkText(
                                text = stringResource(R.string.profile_age_not_set),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = FontStyle.Italic,
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileReadOnlySectionHeader(
                            title = stringResource(R.string.profile_section_residence),
                            icon = Icons.Default.LocationOn,
                        )

                        if (locationParts.isNotEmpty() || user.isTraveler) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (locationParts.isNotEmpty()) {
                                    MiraiLinkText(
                                        text = locationParts.joinToString(" • "),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium,
                                    )
                                }
                                if (user.isTraveler) {
                                    androidx.compose.material3.Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                    ) {
                                        MiraiLinkText(
                                            text = stringResource(R.string.card_traveler_badge),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        androidx.compose.material3.HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outlineVariant,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileReadOnlySectionHeader(
                            title = stringResource(R.string.profile_section_interests),
                            icon = Icons.Default.Favorite,
                        )
                        // Secciones: anime y videojuegos
                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_animes),
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InterestsGrid(
                            items = user.animes.map { it.toInterestItemData() },
                            emptyText = stringResource(id = R.string.user_card_fav_animes_empty),
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games),
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        InterestsGrid(
                            items = user.games.map { it.toInterestItemData() },
                            emptyText = stringResource(id = R.string.user_card_fav_games_empty),
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Curiosidades Gamer
                        if (user.prompts.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            androidx.compose.material3.HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfileReadOnlySectionHeader(
                                title = stringResource(R.string.profile_section_facts),
                                icon = Icons.Default.Info,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            user.prompts.take(3).forEach { prompt ->
                                GamerPromptCard(prompt = prompt)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Informacion personal categorizada
                        val categories = buildCategorizedPersonalInfo(user)
                        if (categories.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(20.dp))
                            androidx.compose.material3.HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            ProfileReadOnlySectionHeader(
                                title = stringResource(R.string.profile_section_personal),
                                icon = Icons.Default.Info,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            CategorizedPersonalInfoSection(user = user)
                        }
                    }

                    Spacer(modifier = Modifier.height(64.dp))
                }
            }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (editUiState != null && editUiState.isEditing) {
                    MiraiLinkButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSave,
                        content = {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.save),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            MiraiLinkText(
                                text = stringResource(id = R.string.save),
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        },
                    )
                } else if (isPreviewMode) {
                    MiraiLinkButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onEdit?.invoke(true) },
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                        Spacer(modifier = Modifier.width(8.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.edit),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileReadOnlySectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(23.dp),
                )
            }
        }
        MiraiLinkText(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ProfileAttributeSelectRow(
    label: String,
    currentValue: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                MiraiLinkText(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(2.dp))
                MiraiLinkText(
                    text = currentValue ?: stringResource(R.string.profile_option_not_specified),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (currentValue != null) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (currentValue != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

private fun getSingleOptionLabel(options: List<CatalogItemOptionDto>?, id: String?): String? =
    options?.find { it.id == id }?.let { it.label ?: it.question ?: it.code }

private fun getMultiOptionLabels(options: List<CatalogItemOptionDto>?, ids: List<String>): String? {
    if (ids.isEmpty() || options == null) return null
    val labels = ids.mapNotNull { id -> options.find { it.id == id }?.let { it.label ?: it.question ?: it.code } }
    return if (labels.isNotEmpty()) labels.joinToString(", ") else null
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
private fun UserCardPreview() {
    UserCard(
        user =
            UserViewEntry(
                id = "1",
                username = "FeryaelJustice",
                nickname = "Feryael Justice",
                bio = @Suppress("ktlint:standard:max-line-length")
                "Hola, soy Feryael Justice. Soy un fanático de anime y videojuegos. Me gustan los personajes y la diversidad de temas en estos juegos.",
                phoneNumber = "604892842",
                animes =
                    listOf(
                        AnimeViewEntry(
                            id = "1",
                            name = "Naruto",
                            imageUrl = null,
                        ),
                        AnimeViewEntry(
                            id = "2",
                            name = "One Punch Man",
                            imageUrl = null,
                        ),
                        AnimeViewEntry(
                            id = "3",
                            name = "Dragon Ball Z",
                            imageUrl = null,
                        ),
                    ),
                games =
                    listOf(
                        GameViewEntry(
                            id = "1",
                            name = "Final Fantasy VII",
                            imageUrl = null,
                        ),
                        GameViewEntry(
                            id = "2",
                            name = "Soul Calibur V",
                            imageUrl = null,
                        ),
                        GameViewEntry(
                            id = "3",
                            name = "The Legend of Zelda: Breath of the Wild",
                            imageUrl = null,
                        ),
                    ),
                email = "adj@jormail.com",
                gender = "Mujer",
                birthdate = "23-12",
            ),
        isPreviewMode = true,
        onEdit = {},
        onSave = {},
    )
}
