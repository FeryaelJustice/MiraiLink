/**
 * @author Feryael Justice
 * @date 24/07/2024
 */
package com.feryaeljustice.mirailink.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toAnimeViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toGameViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toPhotoSlotViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.data.util.deleteTempFile
import com.feryaeljustice.mirailink.data.util.isTempFile
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.DeleteUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase
import com.feryaeljustice.mirailink.domain.util.Logger
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.viewentries.media.PhotoSlotViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val deleteUserPhotoUseCase: DeleteUserPhotoUseCase,
    private val getAnimesUseCase: GetAnimesUseCase,
    private val getGamesUseCase: GetGamesUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    sealed class ProfileUiState {
        object Idle : ProfileUiState()

        object Loading : ProfileUiState()

        data class Success(
            val user: UserViewEntry?,
        ) : ProfileUiState()

        data class Error(
            val message: String,
            val exception: Throwable? = null,
        ) : ProfileUiState()
    }

    val state: StateFlow<ProfileUiState>
        field = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)

    val editState: StateFlow<EditProfileUiState>
        field = MutableStateFlow<EditProfileUiState>(EditProfileUiState())

    // Para comunicar eventos de edición al UI
    private val _editProfUiEvent = MutableSharedFlow<EditProfileUiEvent>(replay = 0)
    val editProfUiEvent = _editProfUiEvent.asSharedFlow()

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            state.value = ProfileUiState.Loading
            val result =
                withContext(ioDispatcher) {
                    getCurrentUserUseCase()
                }

            state.value =
                when (result) {
                    is MiraiLinkResult.Success -> {
                        val user = result.data
                        ProfileUiState.Success(user.toUserViewEntry())
                    }

                    is MiraiLinkResult.Error ->
                        ProfileUiState.Error(
                            result.message,
                            result.exception,
                        )
                } as ProfileUiState.Idle
        }
    }

    fun setIsInEditMode(isEdit: Boolean) {
        editState.update { it.copy(isEditing = isEdit) }
    }

    // Intent es para eventos de la ui al viewmodel, el uiEvent es para lo contrario
    fun onIntent(intent: EditProfileIntent) {
        editState.update { state ->
            when (intent) {
                is EditProfileIntent.Initialize -> {
                    val user = intent.user
                    val photos = MutableList(4) { PhotoSlotViewEntry() }

                    user.photos.forEach {
                        // Importante aqui este sync con la bdd si empieza las position en 0 o 1
                        // Hacemos -1 porque la posicion empiezan en 1 y no en 0 en bdd,
                        // pero aqui empiezan en 0
                        if (it.position in 1..4) {
                            photos[it.position - 1] = it.toPhotoSlotViewEntry()
                        }
                        // RECORDAR!! : Al subir las imagenes sumar +1 a la posicion para la bdd
                    }

                    // Cargar catalogo
                    loadCatalogIfNeeded()

                    state.copy(
                        isEditing = true,
                        nickname = user.nickname,
                        bio = user.bio ?: "",
                        gender = user.gender ?: "",
                        birthdate = user.birthdate ?: "",
                        selectedAnimes = user.animes,
                        selectedGames = user.games,
                        photos = photos,
                    )
                }

                EditProfileIntent.Save -> {
                    logger.d(
                        "ProfileViewModel",
                        "Save: ${state.nickname} ${state.bio} ${state.selectedAnimes} ${state.selectedGames}",
                    )

                    viewModelScope.launch {
                        val nickname = state.nickname
                        val bio = state.bio
                        val gender = state.gender.ifBlank { null }
                        val birthdate = state.birthdate.ifBlank { null } // "YYYY-MM-DD"

                        // validación mínima local (opcional)
                        val dateOk = birthdate?.matches(Regex("""\d{4}-\d{2}-\d{2}""")) ?: true
                        if (!dateOk) {
                            _editProfUiEvent.emit(EditProfileUiEvent.ShowError("Fecha de nacimiento inválida"))
                            return@launch
                        }

                        val animesJson = state.selectedAnimes.let { Json.encodeToString(it) }
                        val gamesJson = state.selectedGames.let { Json.encodeToString(it) }

                        val photoUris =
                            state.photos.map { slot ->
                                if (slot.url?.startsWith("http") == true && slot.uri == null) {
                                    null // no reenviamos porque ya está en el backend y no ha cambiado
                                } else {
                                    slot.uri
                                }
                            }

                        val existingPhotoUrls = editState.value.photos.map { it.url }
                        val result =
                            withContext(ioDispatcher) {
                                updateUserProfileUseCase(
                                    nickname = nickname,
                                    bio = bio,
                                    gender = gender,
                                    birthdate = birthdate,
                                    animesJson = animesJson,
                                    gamesJson = gamesJson,
                                    photoUris = photoUris,
                                    existingPhotoUrls = existingPhotoUrls,
                                )
                            }

                        if (result is MiraiLinkResult.Success) {
                            getCurrentUser()
                            _editProfUiEvent.emit(EditProfileUiEvent.ProfileSavedSuccessfully)
                            editState.update { it.copy(isEditing = false) } // aquí cierras modo edición
                        } else if (result is MiraiLinkResult.Error) {
                            _editProfUiEvent.emit(EditProfileUiEvent.ShowError(result.message))
                        }
                    }

                    return@update state // Devuelve sin modificar el estado todavía
                }

                is EditProfileIntent.UpdateTextField -> {
                    when (intent.field) {
                        TextFieldType.NICKNAME -> state.copy(nickname = intent.value)
                        TextFieldType.BIO -> state.copy(bio = intent.value)
                        TextFieldType.GENDER -> state.copy(gender = intent.value)
                        TextFieldType.BIRTHDATE -> state.copy(birthdate = intent.value)
                    }
                }

                is EditProfileIntent.UpdateTags -> {
                    when (intent.field) {
                        TagType.ANIME -> {
                            val mappedAnimes =
                                intent.selected.mapNotNull { animeName ->
                                    state.animeCatalog.find { it.name == animeName }
                                }
                            state.copy(selectedAnimes = mappedAnimes)
                        }

                        TagType.GAME -> {
                            val mappedGames =
                                intent.selected.mapNotNull { gameName ->
                                    state.gameCatalog.find { it.name == gameName }
                                }
                            state.copy(selectedGames = mappedGames)
                        }
                    }
                }

                is EditProfileIntent.ReorderPhoto -> {
                    val photos = state.photos.toMutableList()
                    val temp = photos[intent.from]
                    photos[intent.from] = photos[intent.to].copy(position = intent.from)
                    photos[intent.to] = temp.copy(position = intent.to)
                    state.copy(photos = photos)
                }

                is EditProfileIntent.RemovePhoto -> {
                    viewModelScope.launch {
                        val result =
                            withContext(ioDispatcher) {
                                deleteUserPhotoUseCase(intent.position + 1) // posición real
                            }

                        if (result is MiraiLinkResult.Success) {
                            // Actualiza el estado UI eliminando la foto
                            val updatedPhotos = state.photos.toMutableList()
                            updatedPhotos[intent.position] =
                                PhotoSlotViewEntry(position = intent.position)
                            editState.update { it.copy(photos = updatedPhotos) }

                            getCurrentUser()
                        } else if (result is MiraiLinkResult.Error) {
                            _editProfUiEvent.emit(EditProfileUiEvent.ShowError(result.message))
                        }
                    }

                    return@update state
                }

                is EditProfileIntent.UpdatePhoto -> {
                    val photos = state.photos.toMutableList()
                    photos[intent.position] =
                        PhotoSlotViewEntry(
                            uri = intent.uri,
                            url = intent.uri.toString(),
                            position = intent.position,
                        )
                    state.copy(
                        photos = photos,
                        showPhotoSourceDialog = false,
                        selectedSlotForDialog = null,
                    )
                }

                is EditProfileIntent.OpenPhotoActionDialog -> {
                    val hasPhoto = state.photos.getOrNull(intent.position)?.url != null
                    if (hasPhoto) {
                        state.copy(
                            selectedSlotForDialog = intent.position,
                            showActionDialog = true,
                            showPhotoSourceDialog = false,
                        )
                    } else {
                        state.copy(
                            selectedSlotForDialog = intent.position,
                            showActionDialog = false,
                            showPhotoSourceDialog = true,
                        )
                    }
                }

                EditProfileIntent.ShowPhotoSourceDialog -> {
                    state.copy(
                        showActionDialog = false,
                        showPhotoSourceDialog = true,
                    )
                }

                EditProfileIntent.ClosePhotoDialogs -> {
                    state.copy(
                        selectedSlotForDialog = null,
                        showActionDialog = false,
                        showPhotoSourceDialog = false,
                    )
                }
            }
        }
    }

    fun cleanupTempPhotos() {
        editState.value.photos.forEach { slot ->
            slot.url?.let {
                if (it.startsWith("file://") || it.startsWith("content://") && isTempFile(it)) {
                    deleteTempFile(it)
                }
            }
        }
    }

    fun loadCatalogIfNeeded() {
        val state = editState.value
        if (state.animeCatalog.isNotEmpty() && state.gameCatalog.isNotEmpty()) return

        viewModelScope.launch {
            val animes =
                withContext(ioDispatcher) {
                    val result = getAnimesUseCase()
                    if (result is MiraiLinkResult.Success) result.data.map { it.toAnimeViewEntry() } else emptyList()
                }

            val games =
                withContext(ioDispatcher) {
                    val result = getGamesUseCase()
                    if (result is MiraiLinkResult.Success) result.data.map { it.toGameViewEntry() } else emptyList()
                }

            editState.update { it.copy(animeCatalog = animes, gameCatalog = games) }
        }
    }
}
