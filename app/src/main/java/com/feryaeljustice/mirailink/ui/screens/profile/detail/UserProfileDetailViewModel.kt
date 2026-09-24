package com.feryaeljustice.mirailink.ui.screens.profile.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetUserProfileByUsernameUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserProfileDetailUiState(
    val isLoading: Boolean = false,
    val user: UserViewEntry? = null,
    val error: UiError? = null,
    val isInteracted: Boolean = false,
)

sealed interface UserProfileDetailUiEvent {
    data class MatchCreated(val nickname: String) : UserProfileDetailUiEvent
    object Disliked : UserProfileDetailUiEvent
}

class UserProfileDetailViewModel(
    private val getUserProfileByUsernameUseCase: GetUserProfileByUsernameUseCase,
    private val likeUserUseCase: LikeUserUseCase,
    private val dislikeUserUseCase: DislikeUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileDetailUiState(isLoading = true))
    val uiState: StateFlow<UserProfileDetailUiState> = _uiState.asStateFlow()

    private val _events = Channel<UserProfileDetailUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun loadProfile(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getUserProfileByUsernameUseCase(username)) {
                is MiraiLinkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = result.data.toUserViewEntry(),
                            error = null,
                        )
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toUiError(),
                        )
                    }
                }
            }
        }
    }

    fun likeUser() {
        val user = _uiState.value.user ?: return
        viewModelScope.launch {
            when (val result = likeUserUseCase(toUserId = user.id)) {
                is MiraiLinkResult.Success -> {
                    _uiState.update { it.copy(isInteracted = true) }
                    if (result.data) {
                        _events.send(UserProfileDetailUiEvent.MatchCreated(user.nickname))
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update { it.copy(error = result.error.toUiError()) }
                }
            }
        }
    }

    fun dislikeUser() {
        val user = _uiState.value.user ?: return
        viewModelScope.launch {
            when (val result = dislikeUserUseCase(toUserId = user.id)) {
                is MiraiLinkResult.Success -> {
                    _uiState.update { it.copy(isInteracted = true) }
                    _events.send(UserProfileDetailUiEvent.Disliked)
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update { it.copy(error = result.error.toUiError()) }
                }
            }
        }
    }
}
