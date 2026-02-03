package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ProfilePictureViewModel(
    private val uploadUserPhotoUseCase: UploadUserPhotoUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val uploadResult: StateFlow<MiraiLinkResult<String>?>
        field = MutableStateFlow<MiraiLinkResult<String>?>(null)

    fun uploadImage(uri: Uri) {
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    uploadUserPhotoUseCase(uri)
                }

            uploadResult.value = result
        }
    }

    fun clearResult() {
        uploadResult.value = null
    }
}
