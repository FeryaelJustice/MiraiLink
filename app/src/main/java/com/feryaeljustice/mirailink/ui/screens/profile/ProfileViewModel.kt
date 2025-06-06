package com.feryaeljustice.mirailink.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.util.deleteTempFile
import com.feryaeljustice.mirailink.data.util.isTempFile
import com.feryaeljustice.mirailink.domain.enums.TagType
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileIntent
import com.feryaeljustice.mirailink.ui.screens.profile.edit.EditProfileUiState
import com.feryaeljustice.mirailink.ui.viewentities.PhotoSlotViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getCurrentUserUseCase: GetCurrentUserUseCase) :
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

    fun onIntent(intent: EditProfileIntent) {
        _editState.update { state ->
            when (intent) {
                is EditProfileIntent.UpdateTextField -> when (intent.field) {
                    TextFieldType.NICKNAME -> state.copy(nickname = intent.value)
                    TextFieldType.BIO -> state.copy(bio = intent.value)
                }

                is EditProfileIntent.UpdateTags -> when (intent.field) {
                    TagType.ANIME -> state.copy(selectedAnimes = intent.selected)
                    TagType.GAME -> state.copy(selectedGames = intent.selected)
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

                is EditProfileIntent.ShowPhotoSourceDialog -> {
                    state.copy(
                        showActionDialog = false,
                        showPhotoSourceDialog = true
                    )
                }

                is EditProfileIntent.ClosePhotoDialogs -> {
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

}