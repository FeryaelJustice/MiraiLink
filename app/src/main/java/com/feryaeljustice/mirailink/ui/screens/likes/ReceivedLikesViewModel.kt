package com.feryaeljustice.mirailink.ui.screens.likes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent
import com.feryaeljustice.mirailink.domain.usecase.swipe.GetReceivedLikesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.calculateAge
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
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
    val photoPresentation: com.feryaeljustice.mirailink.domain.model.capsule.PhotoPresentation? = null,
    val likeId: String,
    val userId: String,
    val username: String,
    val nickname: String,
    val age: Int?,
    val avatarUrl: String?,
    val likedAt: String,
    val discoveryMode: String = "classic",
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
    private val globalMiraiLinkSession: GlobalMiraiLinkSession,
    private val analytics: AnalyticsTracker? = null,
    private val crashReporter: CrashReporter? = null,
) : com.feryaeljustice.mirailink.ui.error.RetryableViewModel() {

    private val _uiState = MutableStateFlow(
        ReceivedLikesUiState(
            isLoading = globalMiraiLinkSession.isPremium.value,
            isPremiumLocked = !globalMiraiLinkSession.isPremium.value,
        ),
    )
    val uiState: StateFlow<ReceivedLikesUiState> = _uiState.asStateFlow()

    private val _events = Channel<ReceivedLikesUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            globalMiraiLinkSession.isPremium.collect { isPrem ->
                _uiState.update { it.copy(isPremiumLocked = !isPrem) }
                if (isPrem) {
                    loadLikes()
                } else {
                    analytics?.logEvent(
                        MiraiAnalyticsEvent.LikesEvent.ReceivedLikesViewed(
                            totalCount = 0,
                            blurredCount = 0,
                        ),
                    )
                    _uiState.update { it.copy(isLoading = false, isRefreshing = false, likes = emptyList()) }
                }
            }
        }
    }

    fun loadLikes(refresh: Boolean = false) {
        if (!globalMiraiLinkSession.isPremium.value) {
            _uiState.update { it.copy(isLoading = false, isRefreshing = false, isPremiumLocked = true) }
            return
        }
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
                            photoPresentation = like.user.photoPresentation,
                            likeId = like.likeId,
                            userId = like.user.id,
                            username = like.user.username,
                            nickname = like.user.nickname.ifBlank { like.user.username },
                            age = calculateAge(like.user.birthdate),
                            avatarUrl = like.user.photos.firstOrNull()?.url,
                            likedAt = like.likedAt,
                            discoveryMode = like.discoveryMode,
                        )
                    }
                    analytics?.logEvent(
                        MiraiAnalyticsEvent.LikesEvent.ReceivedLikesViewed(
                            totalCount = items.size,
                            blurredCount = 0,
                        ),
                    )
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
                    crashReporter?.recordException(
                        throwable = Exception("Get received likes error: ${result.error}"),
                        reason = "Received likes fetch failed",
                        screen = "received_likes",
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = errorFor(result.error) { loadLikes() },
                        )
                    }
                }
            }
        }
    }

    fun matchUser(item: ReceivedLikeItemViewEntry) {
        analytics?.logEvent(
            MiraiAnalyticsEvent.LikesEvent.ReceivedLikeMatched(targetUserId = item.userId),
        )
        viewModelScope.launch {
            when (val result = likeUserUseCase(toUserId = item.userId, receivedLikeId = item.likeId, discoveryMode = item.discoveryMode)) {
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
                    crashReporter?.recordException(
                        throwable = Exception("Match received like error: ${result.error}"),
                        reason = "Match user failed",
                        screen = "received_likes",
                        metadata = mapOf("target_user_id" to item.userId),
                    )
                    _uiState.update { it.copy(error = errorFor(result.error) { matchUser(item) }) }
                }
            }
        }
    }
}
