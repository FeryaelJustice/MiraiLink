package com.feryaeljustice.mirailink.ui.screens.likes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.usecase.swipe.GetReceivedLikesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.calculateAge
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReceivedLikeItemViewEntry(
    val likeId: String,
    val userId: String,
    val username: String,
    val nickname: String,
    val age: Int?,
    val avatarUrl: String?,
    val likedAt: String,
)

data class ReceivedLikesUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isPremiumLocked: Boolean = false,
    val likes: List<ReceivedLikeItemViewEntry> = emptyList(),
    val error: UiError? = null,
)

sealed interface ReceivedLikesUiEvent {
    data class MatchCreated(val nickname: String) : ReceivedLikesUiEvent
}

class ReceivedLikesViewModel(
    private val getReceivedLikesUseCase: GetReceivedLikesUseCase,
    private val likeUserUseCase: LikeUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceivedLikesUiState(isLoading = true))
    val uiState: StateFlow<ReceivedLikesUiState> = _uiState.asStateFlow()

    private val _events = Channel<ReceivedLikesUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadLikes()
    }

    fun loadLikes(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _uiState.update { it.copy(isRefreshing = true, error = null) }
            } else {
                _uiState.update { it.copy(isLoading = true, error = null) }
            }

            when (val result = getReceivedLikesUseCase()) {
                is MiraiLinkResult.Success -> {
                    val items = result.data.map { like ->
                        ReceivedLikeItemViewEntry(
                            likeId = like.likeId,
                            userId = like.user.id,
                            username = like.user.username,
                            nickname = like.user.nickname.ifBlank { like.user.username },
                            age = calculateAge(like.user.birthdate),
                            avatarUrl = like.user.photos.firstOrNull()?.url,
                            likedAt = like.likedAt,
                        )
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            likes = items,
                            error = null,
                        )
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = result.error.toUiError(),
                        )
                    }
                }
            }
        }
    }

    fun matchUser(item: ReceivedLikeItemViewEntry) {
        viewModelScope.launch {
            when (val result = likeUserUseCase(toUserId = item.userId)) {
                is MiraiLinkResult.Success -> {
                    // Remove from list
                    _uiState.update { state ->
                        state.copy(likes = state.likes.filter { it.userId != item.userId })
                    }
                    if (result.data) {
                        _events.send(ReceivedLikesUiEvent.MatchCreated(item.nickname))
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update { it.copy(error = result.error.toUiError()) }
                }
            }
        }
    }
}
