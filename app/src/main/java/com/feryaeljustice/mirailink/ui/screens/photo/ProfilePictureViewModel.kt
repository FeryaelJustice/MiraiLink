package com.feryaeljustice.mirailink.ui.screens.photo

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.photos.UploadUserPhotoUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfilePictureViewModel(
    private val uploadUserPhotoUseCase: UploadUserPhotoUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {
    val uploadSucceeded: StateFlow<Boolean>
        field = MutableStateFlow(false)

    val error: StateFlow<UiError?>
        field = MutableStateFlow<UiError?>(null)

    fun uploadImage(uri: Uri) {
        setRecoveryAction { uploadImage(uri) }
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    uploadUserPhotoUseCase(uri)
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    uploadSucceeded.value = true
                    error.value = null
                }
                is MiraiLinkResult.Error -> error.value = result.error.toUiError()
            }
        }
    }

    fun clearResult() {
        uploadSucceeded.value = false
        error.value = null
    }
}
