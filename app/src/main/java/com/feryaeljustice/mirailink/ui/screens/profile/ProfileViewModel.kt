package com.feryaeljustice.mirailink.ui.screens.profile

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.util.deleteTempFile
import com.feryaeljustice.mirailink.data.util.isTempFile
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.user.User
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetAnimesUseCase
import com.feryaeljustice.mirailink.domain.usecase.catalog.GetGamesUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.UpdateUserProfileUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiEvent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.viewentities.PhotoSlotViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getAnimesUseCase: GetAnimesUseCase,
    private val getGamesUseCase: GetGamesUseCase,
) :
    ViewModel() {

    sealed class ProfileUiState {
        object Idle : ProfileUiState()
        object Loading : ProfileUiState()
        data class Success(val user: User?) : ProfileUiState()
        data class Error(val message: String, val exception: Throwable? = null) : ProfileUiState()
    }

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val state = _state.asStateFlow()

    private val _editState = MutableStateFlow(EditProfileUiState())
    val editState = _editState.asStateFlow()

    // Para comunicar eventos de edición al UI
    private val _editProfUiEvent = MutableSharedFlow<EditProfileUiEvent>(replay = 0)
    val editProfUiEvent = _editProfUiEvent.asSharedFlow()

    init {
        getCurrentUser()
    }

    fun getCurrentUser() {
        _state.value = ProfileUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    _state.value = ProfileUiState.Success(result.data)
                }

                is MiraiLinkResult.Error -> {
                    _state.value = ProfileUiState.Error(result.message, result.exception)
                }
            }
        }
    }

    fun setIsInEditMode(isEdit: Boolean) {
        _editState.value = _editState.value.copy(isEditing = isEdit)
    }

    // Intent es para eventos de la ui al viewmodel, el uiEvent es para lo contrario
    fun onIntent(intent: EditProfileIntent) {
        _editState.update { state ->
            when (intent) {
                is EditProfileIntent.Initialize -> {
                    val user = intent.user
                    val photos = MutableList(4) { PhotoSlotViewEntity() }

                    user.photos.forEach {
                        // Importante aqui este sync con la bdd si empieza las position en 0 o 1
                        // Hacemos -1 porque la posicion empiezan en 1 y no en 0 en bdd,
                        // pero aqui empiezan en 0
                        if (it.position in 0..3) {
                            photos[it.position - 1] =
                                PhotoSlotViewEntity(url = it.url, position = it.position)
                        }
                        // RECORDAR!! : Al subir las imagenes sumar +1 a la posicion para la bdd
                    }

                    // Cargar catalogo
                    loadCatalogIfNeeded()

                    state.copy(
                        isEditing = true,
                        nickname = user.nickname,
                        bio = user.bio ?: "",
                        selectedAnimes = user.animes,
                        selectedGames = user.games,
                        photos = photos
                    )
                }

                EditProfileIntent.Save -> {
                    Log.d(
                        "ProfileViewModel",
                        "Save: ${state.nickname} ${state.bio} ${state.selectedAnimes} ${state.selectedGames}"
                    )

                    val nickname = state.nickname
                    val bio = state.bio
                    val animesJson = state.selectedAnimes.let { Json.encodeToString(it) }
                    val gamesJson = state.selectedGames.let { Json.encodeToString(it) }

//                    val photosJson = Json.encodeToString(
//                        state.photos.mapNotNullIndexed { index, slot ->
//                            slot.url?.let { UploadPhotoDto(position = index + 1, url = it) }
//                        }
//                    )

                    val photoUris = state.photos.map { slot ->
                        slot.url?.takeIf {
                            it.startsWith("content://") || it.startsWith("file://")
                        }?.toUri()
                    }

                    viewModelScope.launch {
                        val result = updateUserProfileUseCase(
                            nickname = nickname,
                            bio = bio,
                            animesJson = animesJson,
                            gamesJson = gamesJson,
                            photoUris = photoUris
                        )

                        if (result is MiraiLinkResult.Success) {
                            _editProfUiEvent.emit(EditProfileUiEvent.ProfileSavedSuccessfully)
                            getCurrentUser()
                            _editState.update { it.copy(isEditing = false) } // aquí cierras modo edición
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
                    }
                }

                is EditProfileIntent.UpdateTags -> {
                    when (intent.field) {
                        TagType.ANIME -> {
                            val mappedAnimes = intent.selected.mapNotNull { animeName ->
                                state.animeCatalog.find { it.name == animeName }
                            }
                            state.copy(selectedAnimes = mappedAnimes)
                        }

                        TagType.GAME -> {
                            val mappedGames = intent.selected.mapNotNull { gameName ->
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
                    val photos = state.photos.toMutableList()
                    photos[intent.position] = PhotoSlotViewEntity(url = null)
                    state.copy(photos = photos)
                }

                is EditProfileIntent.UpdatePhoto -> {
                    val photos = state.photos.toMutableList()
                    photos[intent.position] = PhotoSlotViewEntity(
                        url = intent.uri.toString(), // o guarda el URI directamente
                        position = intent.position
                    )
                    state.copy(
                        photos = photos,
                        showPhotoSourceDialog = false,
                        selectedSlotForDialog = null
                    )
                }

                is EditProfileIntent.OpenPhotoActionDialog -> {
                    val hasPhoto = state.photos.getOrNull(intent.position)?.url != null
                    if (hasPhoto) {
                        state.copy(
                            selectedSlotForDialog = intent.position,
                            showActionDialog = true,
                            showPhotoSourceDialog = false
                        )
                    } else {
                        state.copy(
                            selectedSlotForDialog = intent.position,
                            showActionDialog = false,
                            showPhotoSourceDialog = true
                        )
                    }
                }

                EditProfileIntent.ShowPhotoSourceDialog -> {
                    state.copy(
                        showActionDialog = false,
                        showPhotoSourceDialog = true
                    )
                }

                EditProfileIntent.ClosePhotoDialogs -> {
                    state.copy(
                        selectedSlotForDialog = null,
                        showActionDialog = false,
                        showPhotoSourceDialog = false
                    )
                }
            }
        }
    }

    fun cleanupTempPhotos() {
        _editState.value.photos.forEach { slot ->
            slot.url?.let {
                if (it.startsWith("file://") || it.startsWith("content://") && isTempFile(it)) {
                    deleteTempFile(it)
                }
            }
        }
    }

    fun loadCatalogIfNeeded() {
        val state = _editState.value
        if (state.animeCatalog.isNotEmpty() && state.gameCatalog.isNotEmpty()) return

        viewModelScope.launch {
            val animes = getAnimesUseCase().let {
                if (it is MiraiLinkResult.Success) it.data else emptyList()
            }

            val games = getGamesUseCase().let {
                if (it is MiraiLinkResult.Success) it.data else emptyList()
            }

            _editState.update { it.copy(animeCatalog = animes, gameCatalog = games) }
        }
    }

}