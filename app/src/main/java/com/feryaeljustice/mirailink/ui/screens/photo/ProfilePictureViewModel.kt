package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfilePictureViewModel @Inject constructor(private val uploadUserPhotoUseCase: Lazy<UploadUserPhotoUseCase>) :
    ViewModel() {
    private val _uploadResult = MutableStateFlow<MiraiLinkResult<String>?>(null)
    val uploadResult = _uploadResult.asStateFlow()

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                uploadUserPhotoUseCase.get()(uri)
            }

            _uploadResult.value = result
        }
    }

    fun clearResult() {
        _uploadResult.value = null
    }
}