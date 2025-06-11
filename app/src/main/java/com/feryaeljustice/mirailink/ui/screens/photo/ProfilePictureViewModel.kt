package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePictureViewModel @Inject constructor(private val uploadUserPhotoUseCase: UploadUserPhotoUseCase) :
    ViewModel() {
    private val _uploadResult = MutableStateFlow<MiraiLinkResult<String>?>(null)
    val uploadResult = _uploadResult.asStateFlow()

    fun uploadImage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _uploadResult.value = null
            when (val result = uploadUserPhotoUseCase(uri)) {
                is MiraiLinkResult.Error -> _uploadResult.value = result
                is MiraiLinkResult.Success -> _uploadResult.value = result
            }
        }
    }

    fun clearResult() {
        _uploadResult.value = null
    }
}