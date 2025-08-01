package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.media.EditablePhotoGrid
import com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel
import com.feryaeljustice.mirailink.ui.components.molecules.MultiSelectDropdown
import com.feryaeljustice.mirailink.ui.components.molecules.TagsSection
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.utils.extensions.shadow

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: User,
    canUndo: Boolean = false,
    isPreviewMode: Boolean = false,
    editUiState: EditProfileUiState? = null,
    onValueChange: ((field: TextFieldType, value: String) -> Unit)? = null,
    onTagSelected: ((type: TagType, newValue: List<String>) -> Unit)? = null,
    onPhotoSlotClick: ((Int) -> Unit)? = null,
    onPhotoReorder: ((from: Int, to: Int) -> Unit)? = null,
    onSave: (() -> Unit),
    onLike: (() -> Unit)? = null,
    onGoBackToLast: (() -> Unit)? = null,
    onDislike: (() -> Unit)? = null,
    onEdit: ((Boolean) -> Unit)? = null
) {
    val (focusRequester) = FocusRequester.createRefs()
    val (fullscreenImageUrl, setFullscreenImageUrl) = remember { mutableStateOf<String?>(null) }

    if (fullscreenImageUrl != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                .clickable { setFullscreenImageUrl(null) } // Dismiss al hacer clic fuera
                .padding(8.dp)
                .zIndex(99f)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            MiraiLinkIconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .zIndex(100f),
                onClick = {
                    setFullscreenImageUrl(null)
                }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.content_description_user_card_close_btn),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            AsyncImage(
                model = fullscreenImageUrl,
                contentDescription = stringResource(id = R.string.content_description_user_card_fullscreen_img),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize(0.9f)
                    .padding(all = 8.dp)
                    .clickable(enabled = false) {} // Evita que el clic cierre si se hace en la imagen
            )
        }
    }

    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                color = MaterialTheme.colorScheme.onSurface,
                alpha = 0.5f,
                offsetX = (4).dp,
                offsetY = (4).dp,
                blurRadius = 4.dp,
                shape = RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = BorderStroke(width = 2.dp, MaterialTheme.colorScheme.onSurface)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (editUiState?.isEditing == true) {
                MiraiLinkOutlinedIconButton(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.TopEnd)
                        .alpha(0.8f)
                        .zIndex(10f), // Lo eleva sobre el grid
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    onClick = { onEdit?.invoke(false) },
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.content_description_user_card_close_edit_mode)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (editUiState != null && editUiState.isEditing) Modifier.padding(16.dp) else Modifier
                    )
                    .verticalScroll(rememberScrollState())
            ) {
                if (editUiState != null && editUiState.isEditing) {
                    // Cuadrícula de imágenes
                    EditablePhotoGrid(
                        photos = editUiState.photos,
                        onSlotClick = onPhotoSlotClick,
                        onPhotoReorder = onPhotoReorder
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // TextField para nombre
                    MiraiLinkOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = editUiState.nickname,
                        onValueChange = { onValueChange?.invoke(TextFieldType.NICKNAME, it) },
                        label = stringResource(id = R.string.user_card_nickname),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusRequester.requestFocus()
                            }
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // TextField para bio
                    MiraiLinkOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        value = editUiState.bio,
                        onValueChange = { onValueChange?.invoke(TextFieldType.BIO, it) },
                        label = stringResource(id = R.string.user_card_bio),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Dropdowns de anime/videojuegos (con MultiSelect o Chips según preferencia visual)
                    MultiSelectDropdown(
                        label = stringResource(id = R.string.user_card_fav_animes),
                        options = editUiState.animeCatalog.map { it.name },
                        selected = editUiState.selectedAnimes.map { it.name },
                        onSelectionChange = { onTagSelected?.invoke(TagType.ANIME, it) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MultiSelectDropdown(
                        label = stringResource(id = R.string.user_card_fav_games),
                        options = editUiState.gameCatalog.map { it.name },
                        selected = editUiState.selectedGames.map { it.name },
                        onSelectionChange = { onTagSelected?.invoke(TagType.GAME, it) }
                    )

                    Spacer(modifier = Modifier.height(64.dp))
                } else {
                    PhotoCarousel(
                        photoUrls = user.photos.map { it.url },
                        onLongPressOnImage = { url ->
                            setFullscreenImageUrl(url)
                        })

                    Column(modifier = Modifier.padding(16.dp)) {
                        MiraiLinkText(
                            text = user.nicknameElseUsername(),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall.copy(textDecoration = TextDecoration.Underline),
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        MiraiLinkText(
                            if (!user.bio.isNullOrBlank()) user.bio else stringResource(id = R.string.bio_placeholder),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = FontStyle.Italic,
                        )

                        // Secciones: anime y videojuegos
                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_animes),
                            fontWeight = FontWeight.SemiBold
                        )
                        user.animes.takeIf { it.isNotEmpty() }?.let { animes ->
                            TagsSection(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                tags = animes.map { it.name })
                        } ?: MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_animes_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontStyle = MaterialTheme.typography.labelSmall.fontStyle,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games),
                            fontWeight = FontWeight.SemiBold
                        )
                        user.games.takeIf { it.isNotEmpty() }?.let { games ->
                            TagsSection(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                tags = games.map { it.name })
                        } ?: MiraiLinkText(
                            text = stringResource(id = R.string.user_card_fav_games_empty),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            fontStyle = MaterialTheme.typography.labelSmall.fontStyle
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(240.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (editUiState != null && editUiState.isEditing) {
                    MiraiLinkButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onSave,
                        content = {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.save)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            MiraiLinkText(
                                text = stringResource(id = R.string.save),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        })
                } else if (isPreviewMode) {
                    MiraiLinkButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onEdit?.invoke(true) },
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit))
                        Spacer(modifier = Modifier.width(8.dp))
                        MiraiLinkText(
                            text = stringResource(id = R.string.edit),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                } else {
                    MiraiLinkOutlinedButton(
                        modifier = Modifier
                            .size(64.dp),
                        onClick = { onDislike?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Blue),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.discard),
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(32.dp))

                    if (canUndo) {
                        MiraiLinkOutlinedButton(
                            modifier = Modifier
                                .size(64.dp),
                            onClick = { onGoBackToLast?.invoke() },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Yellow),
                            shape = CircleShape,
                            border = BorderStroke(1.dp, Color.Black),
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.comeback),
                                tint = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(32.dp))
                    }

                    MiraiLinkOutlinedButton(
                        modifier = Modifier
                            .size(64.dp),
                        onClick = { onLike?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Red),
                        shape = CircleShape,
                        border = BorderStroke(1.dp, Color.Black),
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = stringResource(R.string.like),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserCardPreview() {
    UserCard(
        user = User(
            id = "1",
            username = "FeryaelJustice",
            nickname = "Feryael Justice",
            bio = "Hola, soy Feryael Justice. Soy un fanático de anime y videojuegos. Me gustan los personajes y la diversidad de temas en estos juegos.",
            phoneNumber = "604892842",
            animes = listOf(
                Anime(
                    id = "1",
                    name = "Naruto",
                    imageUrl = null,
                ),
                Anime(
                    id = "2",
                    name = "One Punch Man",
                    imageUrl = null,
                ),
                Anime(
                    id = "3",
                    name = "Dragon Ball Z",
                    imageUrl = null,
                )
            ),
            games = listOf(
                Game(
                    id = "1",
                    name = "Final Fantasy VII",
                    imageUrl = null,
                ),
                Game(
                    id = "2",
                    name = "Soul Calibur V",
                    imageUrl = null,
                ),
                Game(
                    id = "3",
                    name = "The Legend of Zelda: Breath of the Wild",
                    imageUrl = null,
                )
            ),
            email = "adj@jormail.com",
            gender = "Mujer",
            birthdate = "23-12"
        ),
        isPreviewMode = true,
        onLike = {},
        onDislike = {},
        onEdit = {},
        onSave = {}
    )
}