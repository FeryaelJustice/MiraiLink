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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.feryaeljustice.mirailink.ui.components.media.EditablePhotoGrid
import com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview
import com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel
import com.feryaeljustice.mirailink.ui.components.molecules.BirthdateField
import com.feryaeljustice.mirailink.ui.components.molecules.GenderSelector
import com.feryaeljustice.mirailink.ui.components.molecules.MultiSelectDropdown
import com.feryaeljustice.mirailink.ui.components.molecules.MultiSelectOption
import com.feryaeljustice.mirailink.ui.components.molecules.ResidenceSelector
import com.feryaeljustice.mirailink.ui.components.molecules.TagsSection
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.utils.extensions.localizedLabel
import com.feryaeljustice.mirailink.ui.utils.extensions.shadow
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

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

                    // Selector de género
                    GenderSelector(
                        gender = Gender.fromRealValue(editUiState.gender) ?: Gender.Other,
                        onChange = { genderEnum ->
                            onValueChange?.invoke(TextFieldType.GENDER, genderEnum.realValue)
                        },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // TextField para fecha de nacimiento
                    BirthdateField(
                        birthdateIso = toBackendDate(editUiState.birthdate),
                        onChange = { onValueChange?.invoke(TextFieldType.BIRTHDATE, it) },
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    Spacer(modifier = Modifier.height(16.dp))

                    MiraiLinkText(
                        text = stringResource(R.string.profile_section_interests),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Dropdowns de anime/videojuegos (con MultiSelect o Chips según preferencia visual)
                    MultiSelectDropdown(
                        label = stringResource(id = R.string.user_card_fav_animes),
                        options = editUiState.animeCatalog.map { MultiSelectOption(it.id, it.name) },
                        selected = editUiState.selectedAnimes.map { it.id },
                        onSelectionChange = { onTagSelect?.invoke(TagType.ANIME, it) },
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MultiSelectDropdown(
                        label = stringResource(id = R.string.user_card_fav_games),
                        options = editUiState.gameCatalog.map { MultiSelectOption(it.id, it.name) },
                        selected = editUiState.selectedGames.map { it.id },
                        onSelectionChange = { onTagSelect?.invoke(TagType.GAME, it) },
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
                        user.animes.takeIf { it.isNotEmpty() }?.let { animes ->
                            TagsSection(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                tags = animes.map { it.name },
                            )
                        } ?: MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_animes_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games),
                            fontWeight = FontWeight.SemiBold,
                        )
                        user.games.takeIf { it.isNotEmpty() }?.let { games ->
                            TagsSection(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                tags = games.map { it.name },
                            )
                        } ?: MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
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
