package com.feryaeljustice.mirailink.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val getCurrentUserUseCase: GetCurrentUserUseCase) : ViewModel() {

    sealed class ProfileUiState {
        object Idle : ProfileUiState()
        object Loading : ProfileUiState()
        data class Success(val user: User?) : ProfileUiState()
        data class Error(val message: String, val exception: Throwable? = null) : ProfileUiState()
    }

    private val _state = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val state = _state.asStateFlow()

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
}